package com.example.opsc7312_regularbirds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVadapter_NearByHotspots(var observationView: List<Locations>) : RecyclerView.Adapter<RVadapter_NearByHotspots.ViewHolder>()  {
    public var itemClickListener: RVadapter_NearByHotspots.OnItemClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVadapter_NearByHotspots.ViewHolder {
        val observationView = LayoutInflater.from(parent.context).inflate(R.layout.template_observation_listitems,
            parent,false)
        return ViewHolder(observationView)
    }

    override fun onBindViewHolder(holder: RVadapter_NearByHotspots.ViewHolder, position: Int) {
        val currentItem = observationView[position]

        holder.itemView.setOnClickListener {
            val clickedItemId = currentItem.obsvId
            itemClickListener?.OnItemClick(clickedItemId)
        }
        holder.commonName.text = currentItem.comName
        holder.genLocation.text = currentItem.locName
    }

    override fun getItemCount(): Int {
        return observationView.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val commonName : TextView = itemView.findViewById(R.id.txtObsvTitle)
        val genLocation : TextView = itemView.findViewById(R.id.txtObsvDate)

    }

    interface OnItemClickListener {
        fun OnItemClick(itemId: Int)
    }
    fun setFilteredList(mList: List<Locations>){
        this.observationView = mList
        notifyDataSetChanged()
    }

    fun updateData(newData: List<Locations>) {
        observationView = newData
        notifyDataSetChanged()
    }
}