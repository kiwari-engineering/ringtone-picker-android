package com.tkfbudi.ringtone

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkfbudi.ringtone.RingtoneAdapter.RingtoneViewHolder
import kotlinx.android.synthetic.main.item_ringtone.view.*

/**
 * Created on : 29/09/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */
class RingtoneAdapter(var ringtones: MutableList<Ringtone>, val listener: RingtoneClickedListener): RecyclerView.Adapter<RingtoneViewHolder>() {

    var lastSelected: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_ringtone, parent, false)
        return RingtoneViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ringtones.size
    }

    fun setLastPosition(position: Int) {
        lastSelected = position
    }

    override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
        var ringtone = ringtones.get(holder.adapterPosition)
        holder.rbRingtone.setText(ringtone.name)
        holder.rbRingtone.setOnClickListener {
            lastSelected = holder.adapterPosition
            listener.onItemClicked(ringtone)
            notifyDataSetChanged()
        }
        holder.rbRingtone.isChecked = (lastSelected == position)
    }

    class RingtoneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rbRingtone = view.rbRingtone
    }

    interface RingtoneClickedListener {
        fun onItemClicked(ringtone: Ringtone)
    }

}