package personal.ianroberts.joiitechnical.modules.database

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import personal.ianroberts.joiitechnical.BuildConfig
import personal.ianroberts.joiitechnical.modules.network.BeerAPI
import javax.inject.Inject

class BeerRepo @Inject constructor(private val beerDao: BeerDao, private val beerAPI: BeerAPI) {

    var dataProviderDisposable: Disposable? = null

    private fun getDbBeers(): Maybe<List<BeerDTO>> {
        //flowable is the equivalent to Livedata, so this will receive updates from Room on a database change
        return beerDao.getAllBeersRx()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .switchMapMaybe {
                if (it.isNotEmpty()) {
                    Maybe.just(it.map { BeerDTO(it) })
                } else {
                    Maybe.empty()
                }
            }
            .firstElement()
    }

    private fun getApiBeers(): Single<List<BeerDTO>> {
        //better handling of API response expected here
        //ie, checking if response.isSuccessful() before mapping body to DTO
        //or .retryWhen { count, throwable -> } which can determine when to retry based on Http Response codes
        return beerAPI.getBeerList(BuildConfig.SANDBOX_KEY)
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.data?.map { BeerDTO(it) }.orEmpty()
            }
            .doOnSuccess {
                beerDao.insert(*it.map { it.toDB() }.toTypedArray())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
            }
            .doOnError {
                it.printStackTrace()
            }
    }

    fun loadData(): Flowable<List<BeerDTO>> {
        val dbObservable = getDbBeers()
        val apiObservable = getApiBeers()

        if (!isNetworkInProgress()) {
            dataProviderDisposable =
                Observable.concat(dbObservable.toObservable(), apiObservable.toObservable())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
        }

        return beerDao.getAllBeersFlowable()
            .subscribeOn(Schedulers.io())
            .map {
                it.map { BeerDTO(it) }
            }
    }

    private fun isNetworkInProgress() = !(dataProviderDisposable?.isDisposed ?: true)
}