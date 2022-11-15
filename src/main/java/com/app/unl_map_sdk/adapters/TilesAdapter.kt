package com.app.unl_map_sdk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.data.TileData
import com.app.unl_map_sdk.data.TileEnum

class TilesAdapter(
    var context: Context,
    var list: Array<TileEnum>,
    var listener: ItemSelectedListener,
) : RecyclerView.Adapter<TilesAdapter.TilesViewHolder>() {
    var selectedItem=0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TilesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tile_selector, parent, false)

        return TilesViewHolder(view)

    }


    inner class TilesViewHolder(dataBinding: View) :
        RecyclerView.ViewHolder(dataBinding) {
        lateinit var ivTile: ImageView
        lateinit var tvTitle: TextView

        fun onBind(item: TileEnum) {
             ivTile = itemView.findViewById(R.id.ivTile)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvTitle.text=item.name
//            ivTile.setImageDrawable(context.resources.getDrawable(item.getImage()))
        }
    }


    override fun onBindViewHolder(holder: TilesViewHolder, position: Int) {
        holder.onBind(list!![position])
        holder.itemView.setOnClickListener {

            selectedItem=position
            notifyDataSetChanged()
        }
        if(selectedItem==position){
            holder.itemView.alpha= 1.0F
            listener.loadStyle(list!![position])
        }else{
            holder.itemView.alpha= 0.5F
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    interface ItemSelectedListener {
        abstract fun loadStyle(tileData: TileEnum)
    }
}
