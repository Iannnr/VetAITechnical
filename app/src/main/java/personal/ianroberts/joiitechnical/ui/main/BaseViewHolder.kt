package personal.ianroberts.joiitechnical.ui.main

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<DATA>(binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun onBind(item: DATA)
}