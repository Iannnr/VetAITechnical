package personal.ianroberts.joiitechnical.modules.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg entity: T): Completable

    @Update
    fun update(vararg entity: T): Single<Int>

    @Delete
    fun delete(vararg entity: T): Single<Int>
}