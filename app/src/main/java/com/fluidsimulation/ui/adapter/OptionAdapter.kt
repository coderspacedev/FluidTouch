package com.fluidsimulation.ui.adapter

import android.app.*
import android.view.*
import androidx.recyclerview.widget.*
import com.fluidsimulation.databinding.*
import com.fluidsimulation.listeners.*

class OptionAdapter(val context: Activity, val listener: CommonListener?) : RecyclerView.Adapter<OptionAdapter.DataViewHolder>() {

    private var options: MutableList<String>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutRowItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun addAll(options: MutableList<String>) {
        options.addAll(options)
        notifyItemRangeChanged(0, options.size)
    }

    override fun getItemCount(): Int {
        return options?.size ?: 0
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val option = options?.get(position)
        context.apply context@{
            holder.binding.apply {
                root.setOnClickListener {
                    listener?.onOpen(option, position)
                }
            }
        }
    }

    fun clear() {
        options?.clear()
    }

    fun getData(): MutableList<String>? {
        return options
    }

    class DataViewHolder(var binding: LayoutRowItemOptionBinding) :
            RecyclerView.ViewHolder(binding.root)
}