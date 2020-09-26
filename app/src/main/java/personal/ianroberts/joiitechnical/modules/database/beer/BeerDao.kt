package personal.ianroberts.joiitechnical.modules.database.beer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import personal.ianroberts.joiitechnical.modules.database.BaseDao

@Dao
interface BeerDao: BaseDao<BeerDB> {

    @Query("SELECT * FROM beer WHERE id = :id LIMIT 1 ")
    fun getBeer(id: String): Maybe<BeerDB>

    @Query("SELECT * FROM beer")
    fun getAllBeersRx(): Maybe<List<BeerDB>>

    @Query("SELECT * FROM beer")
    fun getAllBeersFlowable(): Flowable<List<BeerDB>>

    @Query("SELECT * FROM beer")
    fun getAllBeers(): LiveData<List<BeerDB>>
}