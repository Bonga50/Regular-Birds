package com.iie.opsc7312_regularbirds

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RVadapter_Observations(var observationView: List<BirdObservationModel>) : RecyclerView.Adapter<RVadapter_Observations.ViewHolder>() {
    public var itemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVadapter_Observations.ViewHolder {
        val observationView = LayoutInflater.from(parent.context).inflate(R.layout.template_observation_listitems,
            parent,false)
        return ViewHolder(observationView)
    }

    override fun onBindViewHolder(holder: RVadapter_Observations.ViewHolder, position: Int) {
        val currentItem = observationView[position]

        holder.itemView.setOnClickListener {
            val clickedItemId = currentItem.observationId
            itemClickListener?.OnItemClick(clickedItemId)
        }
        holder.titleView.text = currentItem.observationId.toString()+" ("+currentItem.observationName+") "
        holder.dateView.text = currentItem.observationDate
    }

    override fun getItemCount(): Int {
        return observationView.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleView : TextView = itemView.findViewById(R.id.txtObsvTitle)
        val dateView : TextView = itemView.findViewById(R.id.txtObsvDate)

    }
    interface OnItemClickListener {
        fun OnItemClick(itemId: String)
    }
    fun setFilteredList(mList: List<BirdObservationModel>){
        this.observationView = mList
        notifyDataSetChanged()
    }

    fun updateData(newData: List<BirdObservationModel>) {
        observationView = newData
        notifyDataSetChanged()
    }


}


