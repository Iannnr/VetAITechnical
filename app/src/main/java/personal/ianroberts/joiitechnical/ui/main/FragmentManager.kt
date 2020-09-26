package personal.ianroberts.joiitechnical.ui.main

interface FragmentManager {
    fun showFragment(fragment: BaseFragment<*>)
    fun goBack()
}