package personal.ianroberts.joiitechnical.modules.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BeerDao: BaseDao<BeerDB> {

    @Query("SELECT * FROM beer")
    fun getAllBeersRx(): Maybe<List<BeerDB>>

    @Query("SELECT * FROM beer")
    fun getAllBeersFlowable(): Flowable<List<BeerDB>>

    @Query("SELECT * FROM beer")
    fun getAllBeers(): LiveData<List<BeerDB>>
}