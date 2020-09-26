package personal.ianroberts.joiitechnical.ui.beers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import personal.ianroberts.joiitechnical.modules.database.beer.BeerDTO
import personal.ianroberts.joiitechnical.modules.database.beer.BeerRepo

class BeerListViewModel @ViewModelInject constructor(
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

    fun favourite(beer: BeerDTO, favourited: Boolean, onComplete: () -> Unit) {
        disposables.add(
            repo.favouriteBeer(beer, favourited)
                .subscribeOn(Schedulers.io()) //this ensures the db calls happens on an IO thread
                .observeOn(AndroidSchedulers.mainThread()) //this ensures the subscribe and onComplete happens on the main thread
                .subscribe({ onComplete() }, {})
        )
    }

    fun filterBeers(favourited: Boolean) {
        disposables.add(
            repo.filterBeers(favourited)
                .subscribe({
                    _beers.postValue(it)
                }, {
                    it.printStackTrace()
                })
        )
    }
}