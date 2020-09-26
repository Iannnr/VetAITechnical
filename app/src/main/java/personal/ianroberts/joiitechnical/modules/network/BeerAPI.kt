package personal.ianroberts.joiitechnical.modules.network

import io.reactivex.Observable
import io.reactivex.Single
import personal.ianroberts.joiitechnical.modules.database.beer.BeerList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BeerAPI {

    @GET("beers")
    fun getBeerList(@Query("key") key: String): Single<Response<BeerList>>
}