package com.app.unl_map_sdk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.unl_map_sdk.R
import com.app.unl_map_sdk.data.TileEnum
import com.app.unl_map_sdk.views.UnlMapView

/**
 * Tiles adapter is Adapter for Tiles [RecyclerView] and is used to show list of Tiles for [UnlMapView].
 *
 * @property list This is the list of Tiles for [UnlMapView].
 * @property listener This is used for EventListener like click, Long press etc.
 *
 * In [TilesAdapter] [listener] is used for get the selected Tile from the user and will be loaded on [UnlMapView].
 * @constructor Create empty Tiles adapter.
 */
class TilesAdapter(
    private var list: Array<TileEnum>,
    private var listener: ItemSelectedListener,
) : RecyclerView.Adapter<TilesAdapter.TilesViewHolder>() {

    /**
     * [selectedItem] is used to recognize which Tile is selected and the default value will be Index 0.
     */
    var selectedItem = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TilesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tile_selector, parent, false)
        return TilesViewHolder(view)
    }

    /**
     * [TilesViewHolder] is The ViewHolder for each Tile Item of the List.
     *
     * @constructor
     *
     * @param dataBinding is used as ViewItem for a Tile.
     */
    inner class TilesViewHolder(dataBinding: View) :
        RecyclerView.ViewHolder(dataBinding) {
        private lateinit var ivTile: ImageView
        private lateinit var tvTitle: TextView

        fun onBind(item: TileEnum) {
            ivTile = itemView.findViewById(R.id.ivTile)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = item.name
        }
    }

    override fun onBindViewHolder(holder: TilesViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemView.setOnClickListener {

            selectedItem = position
            notifyDataSetChanged()
        }
        /**
         * Here the purpose of using if condition is to check whether the [selectedItem] is equal to [position]
         * then we load the selected style for Map and also apply the opacity of item to 1 else
         * we set the item opacity to 0.5.
         * */
        if (selectedItem == position) {
            holder.itemView.alpha = 1.0F
            listener.loadStyle(list[position])
        } else {
            holder.itemView.alpha = 0.5F
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    /**
     * [ItemSelectedListener] is an *Interface* and is used as EventListener to load the selected Tile Style for [UnlMapView].
     *
     * @constructor Create empty Item selected listener
     */
    interface ItemSelectedListener {
        fun loadStyle(tileData: TileEnum)
    }
}