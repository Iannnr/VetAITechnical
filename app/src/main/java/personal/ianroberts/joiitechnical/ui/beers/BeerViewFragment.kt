package personal.ianroberts.joiitechnical.ui.beers

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import personal.ianroberts.joiitechnical.R
import personal.ianroberts.joiitechnical.databinding.BeerViewFragmentBinding
import personal.ianroberts.joiitechnical.modules.database.beer.BeerDTO
import personal.ianroberts.joiitechnical.modules.database.beer.BeerRepo
import personal.ianroberts.joiitechnical.ui.main.BaseFragment
import personal.ianroberts.joiitechnical.ui.main.FragmentManager
import javax.inject.Inject

@AndroidEntryPoint
class BeerViewFragment: BaseFragment<BeerViewFragmentBinding>() {

    override val layoutId = R.layout.beer_view_fragment

    companion object {
        //should be using a delegate for arguments like in https://proandroiddev.com/kotlin-delegates-in-android-1ab0a715762d
        fun newInstance(beerDTO: BeerDTO): BeerViewFragment {
            return BeerViewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("item", beerDTO)
                }
            }
        }
    }

    @Inject
    lateinit var beerRepo: BeerRepo

    private val vm: BeerViewModel by viewModels { BeerViewModelFactory(requireArguments().getParcelable("item")!!, beerRepo) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.listenToUpdate()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.beer = it
                binding.executePendingBindings()
            }, {
                it.printStackTrace()
            })

        binding.back.setOnClickListener {
            (activity as? FragmentManager)?.goBack()
        }

        binding.favouriteIcon.setOnClickListener {
            val checked = (it as AppCompatCheckBox).isChecked
            vm.favourite(checked) {
                Snackbar.make(binding.root, getString(R.string.favourited, checked.toString()), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}