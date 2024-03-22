package com.example.customprinterlist.service

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import com.example.customprinterlist.exceptions.DiscoveryFailedException
import com.example.customprinterlist.model.PrinterInfo
import com.example.customprinterlist.utils.log
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.net.InetAddress

internal class PrinterSearcher(private val nsdManager: NsdManager) {
//    private val SERVICE_TYPE = "_pdl-datastream._tcp"
    private val SERVICE_TYPE = "_ipp._tcp" // for printers
//    private val SERVICE_TYPE = "_http._tcp"
//    private val SERVICE_TYPE = "_services._dns-sd._udp"

    fun find(timeout: Long) = Observable.create {

        nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, initDiscoveryListener(it, timeout))
    }

    private fun initDiscoveryListener(emitter: ObservableEmitter<PrinterInfo>, timeout: Long): NsdManager.DiscoveryListener {
        return object : NsdManager.DiscoveryListener {
            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                log("onServiceFound = ${Gson().toJson(serviceInfo)}")
                nsdManager.resolveService(serviceInfo, initResolveListener(emitter))
            }

            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                log("onStopDiscoveryFailed")
                stopDiscovery()
                emitter.onError(DiscoveryFailedException("Discovery failed during onDiscoveryStopped. ErrorCode: $errorCode"))
            }

            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
                log("onStartDiscoveryFailed")
                stopDiscovery()
                emitter.onError(DiscoveryFailedException("Discovery failed during onDiscoveryStarted. ErrorCode: $errorCode"))
            }

            override fun onDiscoveryStarted(serviceType: String?) {
                Handler().postDelayed({ stopDiscovery() }, timeout)
            }

            override fun onDiscoveryStopped(serviceType: String?) {
                log("onDiscoveryStopped")
                emitter.onComplete()
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
                log("onServiceLost = ${Gson().toJson(serviceInfo)}")
            }

            fun stopDiscovery() {
                try {
                    nsdManager.stopServiceDiscovery(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }
    }

    private fun initResolveListener(emitter: ObservableEmitter<PrinterInfo>): NsdManager.ResolveListener {
        return object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                emitter.onNext(PrinterInfo(serviceInfo.serviceName, serviceInfo.host.hostName, serviceInfo.port,false))
            }

            override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                val inet = InetAddress.getByName(serviceInfo.hostAddresses.toString())
                var b: Boolean = false
                b = inet.isReachable(1000)
                emitter.onNext(PrinterInfo(serviceInfo.serviceName, serviceInfo.host.hostName, serviceInfo.port,b))
            }
        }
    }

}