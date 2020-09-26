package personal.ianroberts.joiitechnical.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<DATA> : RecyclerView.Adapter<BaseViewHolder<DATA>>() {

    abstract var differ: AsyncListDiffer<DATA>

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DATA>
    abstract override fun getItemCount(): Int
    abstract override fun onBindViewHolder(holder: BaseViewHolder<DATA>, position: Int)

    fun updateData(data: List<DATA>, onSubmit: () -> Unit) {
        differ.submitList(data, onSubmit)
    }
}