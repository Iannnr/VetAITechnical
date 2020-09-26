package personal.ianroberts.joiitechnical.modules.database.beer

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
                //insert the data into the database
                beerDao.insert(*it.map { it.toDB() }.toTypedArray())
                    .subscribe({}, {
                        it.printStackTrace()
                    })
            }
            .doOnError {
                //expected to use Timer/Firebase exception logging here
                it.printStackTrace()
            }
    }

    /*
        This is what I like about RxJava, the ability to chain and concatenate flows
        This checks the DB and then the API, so that it will always keep a reference to at least one request
     */
    fun loadData(): Flowable<List<BeerDTO>> {
        val dbObservable = getDbBeers()
        val apiObservable = getApiBeers()

        if (!isNetworkInProgress()) {
            dataProviderDisposable =
                Observable.concat(dbObservable.toObservable(), apiObservable.toObservable())
                    .subscribe({}, {
                        //expected to use Timer/Firebase exception logging here
                        it.printStackTrace()
                    })
        }

        //Flowable is the RxJava equivalent to LiveData, so we get to listen to DB updates
        return beerDao.getAllBeersFlowable()
            .subscribeOn(Schedulers.io())
            .map {
                it.map { BeerDTO(it) }
            }
    }

    //get the current beers and map them to filter if the beer is favourited
    fun filterBeers(favourited: Boolean): Flowable<List<BeerDTO>> {
        return beerDao.getAllBeersFlowable()
            .subscribeOn(Schedulers.io())
            .map {
                it.map { BeerDTO(it) }
            }
            .map {
                if (favourited) it.filter { it.favourited } else it
            }
    }

    //takes a beer DTO, creates a DB object from it, sets it as a favourite and updates the db model
    fun favouriteBeer(beer: BeerDTO, favourited: Boolean): Single<Int> {
        return beer.toDB().copy(favourited = favourited).run {
            beerDao.update(this)
        }
    }

    /*
        RxJava doesn't support null data sets, so a Maybe is the Rx Type to support as such.
        If the object doesn't exist in the DB, the Maybe does not complete, and does not update the UI
     */
    fun listenToBeerUpdates(id: String): Maybe<BeerDTO> {
        return beerDao.getBeer(id)
            .subscribeOn(Schedulers.io())
            .map {
                BeerDTO(it)
            }
    }

    //because the API task is a longer-running task than the DB, the disposable isn't disposed until the API call is complete
    private fun isNetworkInProgress() = !(dataProviderDisposable?.isDisposed ?: true)
}