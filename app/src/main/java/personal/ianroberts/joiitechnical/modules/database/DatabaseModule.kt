package personal.ianroberts.joiitechnical.modules.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Provides
    fun provideBeerDatabase(@ApplicationContext context: Context): BeerDatabase {
        return Room.databaseBuilder(context, BeerDatabase::class.java, "beer_database")
            //any changes to the beer schema (or any schemas in the database) will require a migration created her
            //as well as preloading data, presetting executors etc.
            .build()
    }

    @Provides
    fun provideBeerDao(db: BeerDatabase) = db.beerDao()

}