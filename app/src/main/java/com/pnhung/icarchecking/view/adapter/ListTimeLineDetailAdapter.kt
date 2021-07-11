package com.pnhung.icarchecking.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.view.api.model.entities.LocationInfoEntity

class ListTimeLineDetailAdapter(
    val mContext: Context,
    private val listTimeLineDetail: List<LocationInfoEntity>
) :
    RecyclerView.Adapter<ListTimeLineDetailAdapter.Companion.ListTimeLineDetailHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTimeLineDetailHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.item_m004_time_line_detail, parent, false)
        return ListTimeLineDetailHolder(view)
    }

    override fun onBindViewHolder(holder: ListTimeLineDetailHolder, position: Int) {
        val locationInfo = listTimeLineDetail[position]
        holder.tvTime?.text = locationInfo.createdAt?.substring(0,5)
        holder.tvCarStatus?.text = locationInfo.status
        holder.tvCarSpeed?.text = locationInfo.speed
        holder.tvCarAddress?.text = locationInfo.address
    }

    override fun getItemCount(): Int {
        return listTimeLineDetail.size
    }

    companion object {
        class ListTimeLineDetailHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTime: TextView? = view.findViewById(R.id.tv_time)
            val tvCarStatus: TextView? = view.findViewById(R.id.tv_car_status)
            val tvCarSpeed: TextView? = view.findViewById(R.id.tv_car_speed)
            val tvCarAddress: TextView? = view.findViewById(R.id.tv_car_address)
        }

    }
}