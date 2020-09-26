package personal.ianroberts.joiitechnical.modules.database

import personal.ianroberts.joiitechnical.modules.database.beer.Data

data class BeerDTO(val id: String, val name: String, val description: String, val imageUrl: String, val favourited: Boolean) {

    constructor(db: BeerDB): this(db.id, db.name, db.description, db.imageUrl, db.favourited)

    constructor(api: Data): this(api.id, api.name, api.style?.description.orEmpty(), api.labels?.icon.orEmpty(), false)

    //convert from DTO > save into DB
    fun toDB(): BeerDB {
        return BeerDB(id, name, description, imageUrl, favourited)
    }
}