package personal.ianroberts.joiitechnical.ui.main

import personal.ianroberts.joiitechnical.databinding.RowBeerBinding
import personal.ianroberts.joiitechnical.modules.database.BeerDTO

class BeerListViewHolder(private val binding: RowBeerBinding) : BaseViewHolder<BeerDTO>(binding) {

    override fun onBind(item: BeerDTO) {
        //no need to rebind if we're re-using the same model
        if (binding.item == item) return

        binding.item = item
        binding.executePendingBindings()
    }
}