package personal.ianroberts.joiitechnical.ui.beers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import personal.ianroberts.joiitechnical.R
import personal.ianroberts.joiitechnical.databinding.BeerListFragmentBinding
import personal.ianroberts.joiitechnical.ui.main.BaseFragment
import personal.ianroberts.joiitechnical.ui.main.FragmentManager

@AndroidEntryPoint
class BeerListFragment : BaseFragment<BeerListFragmentBinding>() {

    override val layoutId: Int = R.layout.beer_list_fragment

    companion object {
        fun newInstance() = BeerListFragment()
    }

    private val viewModel: BeerListViewModel by viewModels()
    private val adapter = BeerListAdapter(onClickListener = {
        (activity as? FragmentManager)?.showFragment(BeerViewFragment.newInstance(it))
    }, onFavouriteClick = { beer, checked ->
        viewModel.favourite(beer, checked) {
            Snackbar.make(
                binding.root,
                getString(R.string.favourited, checked.toString()),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.beerList.adapter = adapter

        viewModel.beersList.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it) {}
        })

        binding.favouritesOnly.setOnCheckedChangeListener { _, isChecked ->
            viewModel.filterBeers(isChecked)
        }
    }

}