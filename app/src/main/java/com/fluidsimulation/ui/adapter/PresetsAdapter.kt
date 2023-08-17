package com.fluidsimulation.ui.adapter

import android.app.*
import android.view.*
import androidx.recyclerview.widget.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.drawable.*
import com.fluidsimulation.R
import com.fluidsimulation.databinding.*
import com.fluidsimulation.ext.*
import com.fluidsimulation.helper.*
import com.fluidsimulation.listeners.*
import com.fluidsimulation.model.*

class PresetsAdapter(val context: Activity, val listener: CommonListener?) : RecyclerView.Adapter<PresetsAdapter.DataViewHolder>() {

    private var presets: MutableList<PresetData>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutRowItemPresetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun addAll(original: MutableList<PresetData>) {
        presets?.addAll(original)
        notifyItemRangeChanged(0, presets?.size ?: 0)
    }

    override fun getItemCount(): Int {
        return presets?.size ?: 0
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val preset = presets?.get(position)
        context.apply context@{
            holder.binding.apply {
                Glide.with(root.context)
                        .load(preset?.image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
                        .into(imageMedia)
                if (TinyDB(this@context).getInt(SELECTED_PRESET, 0) == position){
                    card.strokeWidth = resources.getDimension(com.intuit.sdp.R.dimen._1sdp).toInt()
                } else {
                    card.strokeWidth = resources.getDimension(R.dimen._0sdp).toInt()
                }
                root.setOnClickListener {
                    val old = TinyDB(this@context).getInt(SELECTED_PRESET, 0)
                    TinyDB(this@context).putInt(SELECTED_PRESET, position)
                    notifyItemChanged(old)
                    notifyItemChanged(position)
                    listener?.onOpen(preset, position)
                }
            }
        }
    }

    fun clear() {
        presets?.clear()
    }

    fun getData(): MutableList<PresetData>? {
        return presets
    }

    class DataViewHolder(var binding: LayoutRowItemPresetBinding) :
            RecyclerView.ViewHolder(binding.root)
}