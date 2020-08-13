package com.example.wbudy_apka.bt

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothSocket
import android.content.Context
import io.flutter.Log
import java.io.IOException
import java.util.*

object Bluetooth {
    val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    val charset = Charsets.US_ASCII
}
fun BluetoothSocket.connectIfNotConnected() {
    if(!this.isConnected) this.connect()
}
