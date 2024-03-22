package com.example.customprinterlist.service

import android.content.Context
import android.net.nsd.NsdManager
import com.example.customprinterlist.model.PrinterInfo
import io.reactivex.Single
import java.util.concurrent.TimeUnit

open class MyPrinterService(private val context: Context) : IPrintService {

    override fun findPrinters(timeout: Long): Single<List<PrinterInfo>> {
        val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
        return PrinterSearcher(nsdManager)
            .find(timeout)
            .distinct()
            .timeout(timeout + 500, TimeUnit.MILLISECONDS)
            .toList()
    }
}

private val TIMEOUT = 4000L

interface IPrintService {
    fun findPrinters(timeout: Long = TIMEOUT): Single<List<PrinterInfo>>
}