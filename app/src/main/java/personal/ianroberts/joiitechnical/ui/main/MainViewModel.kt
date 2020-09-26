package personal.ianroberts.joiitechnical.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import personal.ianroberts.joiitechnical.modules.database.BeerDTO
import personal.ianroberts.joiitechnical.modules.database.BeerRepo

class MainViewModel @ViewModelInject constructor(
    private val repo: BeerRepo
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val disposables = mutableListOf<Disposable>()

    private val _beers = MutableLiveData<List<BeerDTO>>()
    val beersList: LiveData<List<BeerDTO>> get() = _beers

    init {
        disposables.add(
            repo.loadData()
                .subscribe({
                    _beers.postValue(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    override fun onCleared() {
        super.onCleared()

        disposables.onEach { it.dispose() }.clear()
    }
}