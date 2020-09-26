package personal.ianroberts.joiitechnical.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import dagger.hilt.android.AndroidEntryPoint
import personal.ianroberts.joiitechnical.R
import personal.ianroberts.joiitechnical.ui.beers.BeerListFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentManager {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            showFragment(BeerListFragment.newInstance())
        }
    }

    override fun showFragment(fragment: BaseFragment<*>) {
        supportFragmentManager.commit(true) {
            addToBackStack(null)
            replace(R.id.container, fragment, fragment::class.java.canonicalName)
        }
    }

    override fun goBack() {
        onBackPressed()
    }
}