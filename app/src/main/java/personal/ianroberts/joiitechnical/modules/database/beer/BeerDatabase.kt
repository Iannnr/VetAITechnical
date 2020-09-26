package personal.ianroberts.joiitechnical.modules.database.beer

import androidx.room.Database
import androidx.room.RoomDatabase

//could add some DB Views for a performance increase if we're doing complex select statements
@Database(entities = [BeerDB::class], exportSchema = false, version = 1)
abstract class BeerDatabase: RoomDatabase() {

    abstract fun beerDao(): BeerDao
}