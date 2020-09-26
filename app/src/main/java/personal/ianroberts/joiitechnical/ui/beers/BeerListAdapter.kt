package personal.ianroberts.joiitechnical.ui.beers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import personal.ianroberts.joiitechnical.databinding.RowBeerBinding
import personal.ianroberts.joiitechnical.modules.database.beer.BeerDTO
import personal.ianroberts.joiitechnical.ui.main.BaseListAdapter
import personal.ianroberts.joiitechnical.ui.main.BaseViewHolder

class BeerListAdapter(private val onClickListener: (BeerDTO) -> Unit, private val onFavouriteClick: (BeerDTO, Boolean) -> Unit) : BaseListAdapter<BeerDTO>() {

    override var differ = AsyncListDiffer(
        AdapterListUpdateCallback(this),
        AsyncDifferConfig.Builder(BeerItemDiff()).build()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerListViewHolder {
        return BeerListViewHolder(
            RowBeerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BeerDTO>, position: Int) {
        differ.currentList[position]?.let { holder.onBind(it, onClickListener, onFavouriteClick) }
    }

    override fun getItemId(position: Int): Long {
        return differ.currentList[position]?.id.hashCode().toLong()
    }
}