package personal.ianroberts.joiitechnical.ui.beers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import personal.ianroberts.joiitechnical.modules.database.beer.BeerDTO
import personal.ianroberts.joiitechnical.modules.database.beer.BeerRepo

class BeerViewModel(val beer: BeerDTO, private val beerRepo: BeerRepo) : ViewModel() {

    fun favourite(favourited: Boolean, onComplete: () -> Unit) {
        beerRepo.favouriteBeer(beer, favourited)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onComplete()
            }, {
                it.printStackTrace()
            })
    }

    fun listenToUpdate(): Maybe<BeerDTO> {
        return beerRepo.listenToBeerUpdates(beer.id)
    }
}

class BeerViewModelFactory(private val beer: BeerDTO, private val repo: BeerRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BeerDTO::class.java, BeerRepo::class.java)
            .newInstance(beer, repo)
    }
}