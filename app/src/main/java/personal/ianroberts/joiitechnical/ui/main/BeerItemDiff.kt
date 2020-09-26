package personal.ianroberts.joiitechnical.ui.main

import androidx.recyclerview.widget.DiffUtil
import personal.ianroberts.joiitechnical.modules.database.BeerDTO

class BeerItemDiff : DiffUtil.ItemCallback<BeerDTO>() {

    override fun areItemsTheSame(oldItem: BeerDTO, newItem: BeerDTO): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BeerDTO, newItem: BeerDTO): Boolean {
        return oldItem.id == newItem.id && oldItem.favourited == newItem.favourited && oldItem.name == newItem.name && oldItem.description == newItem.description
    }
}