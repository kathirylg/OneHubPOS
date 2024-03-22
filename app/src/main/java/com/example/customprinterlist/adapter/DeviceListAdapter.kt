package com.example.customprinterlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customprinterlist.R
import com.example.customprinterlist.model.PrinterInfo
import com.example.customprinterlist.databinding.NwDeviceItemBinding

class DeviceListAdapter : RecyclerView.Adapter<DeviceListAdapter.MyViewHolder>() {

    private var filteredDataList: ArrayList<PrinterInfo> = ArrayList()

    fun addToList(data: List<PrinterInfo>?) {
        if (data == null){
            this.filteredDataList.clear()
        } else{
            this.filteredDataList.addAll(data)
            this.filteredDataList.sortBy { !it.isReachable }
            this.notifyDataSetChanged()
        }
    }

    class MyViewHolder(var view: NwDeviceItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(data: PrinterInfo) {
            view.deviceName.text = if (data.name.isEmpty()) data.ip else data.name
            if (data.isReachable) {
                view.deviceStatus.text = view.root.context.getString(R.string.lbl_online)
            } else {
                view.deviceStatus.text = view.root.context.getString(R.string.lbl_offline)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val binding = NwDeviceItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredDataList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bind(filteredDataList[p1])
    }
}