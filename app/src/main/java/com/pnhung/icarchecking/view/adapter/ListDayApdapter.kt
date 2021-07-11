package com.pnhung.icarchecking.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pnhung.icarchecking.R
import com.pnhung.icarchecking.view.api.model.entities.LocationInfoEntity
import com.pnhung.icarchecking.view.callback.OnActionCallBack
import com.pnhung.icarchecking.view.fragment.M004TimeLineFrg

class ListDayApdapter(
    val mContext: Context,
    private val mListDay: ArrayList<String>,
    private val mCallBack: OnActionCallBack
) :
    RecyclerView.Adapter<ListDayApdapter.Companion.ListDayHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDayHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.item_m004_list_day, parent, false)
        return ListDayHolder(view, mContext, mCallBack)
    }

    override fun onBindViewHolder(holder: ListDayHolder, position: Int) {
        holder.tvDate?.text = mListDay[position]
    }

    override fun getItemCount(): Int {
        return mListDay.size
    }

    companion object {
        const val KEY_SHOW_TIME_LINE_DETAIL = "KEY_SHOW_TIME_LINE_DETAIL"

        class ListDayHolder(view: View, val context: Context, val callBack: OnActionCallBack) :
            RecyclerView.ViewHolder(view),
            View.OnClickListener, M004TimeLineFrg.OnShowTimeLineDetailCallBack {
            val tvDate: TextView? = view.findViewById(R.id.tv_date)
            private val ivReload: ImageView? = view.findViewById(R.id.iv_reload)
            private val ivShowTrackTimeLine: ImageView? =
                view.findViewById(R.id.iv_show_track_time_line)
            private val ivShowTimelineDetail: ImageView? =
                view.findViewById(R.id.iv_show_time_line_detail)
            private val rvListTimeLineDetail: RecyclerView? =
                view.findViewById(R.id.rv_time_line_detail)
            private var levelImage: Int = 0

            init {
                M004TimeLineFrg.onShowTimeLineDetailCallBack = this
                ivReload?.setOnClickListener(this)
                ivShowTrackTimeLine?.setOnClickListener(this)
                ivShowTimelineDetail?.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                when (v.id) {
                    R.id.iv_reload -> {
                        callBack.callBack(KEY_SHOW_TIME_LINE_DETAIL, tvDate?.text.toString())
                        Toast.makeText(context, "Vai lon", Toast.LENGTH_SHORT).show()
                    }
                    R.id.iv_show_track_time_line -> {

                    }
                    R.id.iv_show_time_line_detail -> {
                        showTimeLineDetail()
                    }
                }
            }

            private fun showTimeLineDetail() {
                if (levelImage == 0) {
                    levelImage = 1
                    ivShowTimelineDetail?.setImageLevel(levelImage)
                    ivReload?.visibility = View.VISIBLE
                    ivShowTrackTimeLine?.visibility = View.VISIBLE
                    rvListTimeLineDetail?.visibility = View.VISIBLE
                    callBack.callBack(KEY_SHOW_TIME_LINE_DETAIL, tvDate?.text.toString())
                } else if (levelImage == 1) {
                    levelImage = 0
                    ivShowTimelineDetail?.setImageLevel(levelImage)
                    ivReload?.visibility = View.INVISIBLE
                    ivShowTrackTimeLine?.visibility = View.INVISIBLE
                    rvListTimeLineDetail?.visibility = View.GONE
                }
            }

            override fun showTimeLineDetail(listTimeLineDetail: List<LocationInfoEntity>) {
                val listTimeLineDetailAdapter =
                    ListTimeLineDetailAdapter(context, listTimeLineDetail)
                rvListTimeLineDetail?.adapter = listTimeLineDetailAdapter
                rvListTimeLineDetail?.layoutManager = GridLayoutManager(context, 1)
            }

        }

    }
}