package com.example.customprinterlist.service

import android.print.PrintAttributes
import android.print.PrintAttributes.Margins
import android.print.PrintAttributes.MediaSize
import android.print.PrintAttributes.Resolution
import android.print.PrinterCapabilitiesInfo
import android.print.PrinterId
import android.print.PrinterInfo
import android.printservice.PrintJob
import android.printservice.PrintService
import android.printservice.PrinterDiscoverySession
import android.util.Log


class CustomPrintService : PrintService() {
    private val TAG = this.javaClass.name

    override fun onCreatePrinterDiscoverySession(): PrinterDiscoverySession? {
        return abc
    }

    override fun onRequestCancelPrintJob(printJob: PrintJob?) {
        Log.d(TAG, "onRequestCancelPrintJob")
    }

    override fun onPrintJobQueued(printJob: PrintJob?) {
        Log.d(TAG, "onPrintJobQueued")
    }

    override fun onConnected() {
        super.onConnected()
        Log.d(TAG,"onRequestCancelPrintJob")
    }

    override fun onDisconnected() {
        super.onDisconnected()
        Log.d(TAG,"onRequestCancelPrintJob")
    }

    private var abc: PrinterDiscoverySession = object : PrinterDiscoverySession() {
        override fun onValidatePrinters(printerIds: List<PrinterId>) {
            Log.d(TAG, "onValidatePrinters")
        }

        override fun onStopPrinterStateTracking(printerId: PrinterId) {
            Log.d(TAG, "onStopPrinterStateTracking")
        }

        override fun onStopPrinterDiscovery() {
            Log.d(TAG, "onStopPrinterDiscovery")
        }

        override fun onStartPrinterStateTracking(printerId: PrinterId) {
            Log.d(TAG, "onStartPrinterStateTracking")
        }

        override fun onStartPrinterDiscovery(priorityList: List<PrinterId>) {
            Log.d(TAG, "onStartPrinterDiscovery")
            /*val printerIDS: ArrayList<PrinterId> = ArrayList()
            priorityList.forEach {
                val printerID = it
                printerIDS.add(printerID);
            }
            removePrinters(printerIDS);

            val printerInfoList = getPrinterList(priorityList);
            addPrinters(printerInfoList)*/
        }

        private fun getPrinterList(priorityList: List<PrinterId>): List<PrinterInfo> {
            val printersInfoList: MutableList<PrinterInfo> = ArrayList()
            for (i in priorityList.indices) {
                val printerID = priorityList[i]
                val capabilities = PrinterCapabilitiesInfo.Builder(printerID)
                    .addMediaSize(MediaSize.ISO_A1, true)
                    .setColorModes(
                        PrintAttributes.COLOR_MODE_COLOR
                                or PrintAttributes.COLOR_MODE_MONOCHROME,
                        PrintAttributes.COLOR_MODE_COLOR
                    )
                    .addResolution(Resolution("R1", "sdfsdf", 600, 600), true)
                    .setMinMargins(Margins(50, 50, 50, 50))
                    .build()
                val mFirstFakePrinter: PrinterInfo = PrinterInfo.Builder(printerID, "ABC$i", PrinterInfo.STATUS_IDLE)
                    .setCapabilities(capabilities).build()
                printersInfoList.add(mFirstFakePrinter)
            }
            return printersInfoList
        }

        override fun onDestroy() {
            Log.d(TAG, "onDestroy")
            //            List<PrinterId> dd = new ArrayList<PrinterId>();
//            dd.add(generatePrinterId("Printer 1"));
//            removePrinters(dd);
        }
    }

}