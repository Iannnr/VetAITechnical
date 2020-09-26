package personal.ianroberts.joiitechnical.ui.beers

import androidx.appcompat.widget.AppCompatCheckBox
import personal.ianroberts.joiitechnical.databinding.RowBeerBinding
import personal.ianroberts.joiitechnical.modules.database.beer.BeerDTO
import personal.ianroberts.joiitechnical.ui.main.BaseViewHolder

class BeerListViewHolder(private val binding: RowBeerBinding) : BaseViewHolder<BeerDTO>(binding) {

    override fun onBind(item: BeerDTO, onClickListener: (BeerDTO) -> Unit, onFavouriteClick: (BeerDTO, Boolean) -> Unit) {
        //no need to rebind if we're re-using the same model
        if (binding.item == item) return

        binding.item = item
        binding.executePendingBindings()

        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }

        binding.favouriteIcon.setOnClickListener {
            onFavouriteClick.invoke(item, (it as AppCompatCheckBox).isChecked)
        }
    }
}