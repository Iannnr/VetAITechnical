package personal.ianroberts.joiitechnical.modules.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beer")
data class BeerDB(@PrimaryKey val id: String, val name: String, val description: String, val imageUrl: String, val favourited: Boolean)