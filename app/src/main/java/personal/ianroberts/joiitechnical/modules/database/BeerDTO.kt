package personal.ianroberts.joiitechnical.modules.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import personal.ianroberts.joiitechnical.modules.network.beer.Data

/*
    Part of the Repository pattern, the App-side should only ever use & care about the DTO
    But this class is used for mapping DTO > DB and API > DTO > DB
    This allows for less changes when the database or API schema changes
    Additionally, Room can create different objects based on class structure, so can help with setting up a DTO from DB
 */
@Parcelize
data class BeerDTO(val id: String, val name: String, val description: String, val imageUrl: String, val favourited: Boolean): Parcelable {

    constructor(db: BeerDB): this(db.id, db.name, db.description, db.imageUrl, db.favourited)

    constructor(api: Data): this(api.id, api.name, api.style?.description.orEmpty(), api.labels?.icon.orEmpty(), false)

    //convert from DTO > save into DB
    fun toDB(): BeerDB {
        return BeerDB(id, name, description, imageUrl, favourited)
    }
}