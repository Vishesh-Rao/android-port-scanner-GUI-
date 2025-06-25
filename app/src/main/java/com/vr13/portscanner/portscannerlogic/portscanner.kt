package com.vr13.portscanner.portscannerlogic

import android.util.Log
import java.net.InetSocketAddress
import java.net.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun scanPorts(
    ip: String,
    portRange: IntRange,
    threads: Int,
    onProgress: (Float) -> Unit = {}
): List<Int> {
    val openPorts = mutableListOf<Int>()
    return withContext(Dispatchers.IO) {
        val total = portRange.count()
        var scanned = 0

        for (port in portRange) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(ip, port), 200)
                socket.close()
                openPorts.add(port)
            } catch (e: Exception) {
                Log.d("PortScanner", "Port $port closed: ${e.message}")
            }
            scanned++
            onProgress(scanned.toFloat() / total)
        }

        openPorts
    }
}
