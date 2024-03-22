package com.example.customprinterlist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customprinterlist.R
import com.example.customprinterlist.adapter.DeviceListAdapter
import com.example.customprinterlist.service.MyPrinterService
import com.example.customprinterlist.utils.log

class MainActivity : AppCompatActivity() {
    private val printService = MyPrinterService(this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var pro: ProgressBar
    private lateinit var deviceListAdapter: DeviceListAdapter
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rcy)
        pro = findViewById(R.id.progress_circular)

        findViewById<Button>(R.id.btn_scan).setOnClickListener{
            pro.visibility = View.VISIBLE
            printService.findPrinters()
                .subscribe( {
                    if(it.isNotEmpty()){
                        log("Printers = $it")
                        runOnUiThread {
                            deviceListAdapter.addToList(it.toList())
                            pro.visibility = View.GONE
                        }
                    }else{
                        Toast.makeText(this,"No Printers", Toast.LENGTH_SHORT).show()
                        runOnUiThread {
                            deviceListAdapter.addToList(null)
                            pro.visibility = View.GONE
                        }
                    }

                }, {

                })
        }
        updateList()
    }
    private fun updateList() {
        recyclerView.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        deviceListAdapter = DeviceListAdapter()
        recyclerView.adapter = deviceListAdapter
    }
}