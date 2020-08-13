package com.example.wbudy_apka.bt

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import io.flutter.Log
import java.io.IOException

class BluetoothServer(private var context: Context): Thread(){

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    val socketServer = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Server_wbudy", Bluetooth.uuid);
    private var shouldLoop: Boolean = false;
    @RequiresApi(Build.VERSION_CODES.M)
    override fun run() {


        Log.i("BT Server Accept Loop","Started ...")
        shouldLoop = true;
        while(shouldLoop) {
            //Log.i("BT Server","Loop ...")
            var socket: BluetoothSocket? = null
            try {
                socket = socketServer?.accept()
            } catch (e: IOException) {
                continue
            }
            if(socket == null) { continue }
            if(!socket.isConnected) {
                socket.connect()
            }
            if(!socket.isConnected) { socket.close(); continue }
            Log.i("BT Server Accept Loop","Connected ...")
            var ok = true;
            when(socket.connectionType) {
                BluetoothSocket.TYPE_RFCOMM -> {
                    ok = true
                }
                else -> {}
            }
            if(!ok) { socket.close(); continue }
            var clientThread = BluetoothServerClientThread(context, socket)
            clientThread.start()
        }
        Log.i("BT Server","Stoped ...")
    }

    override fun interrupt() {
        shouldLoop = false;
        super.interrupt()
    }
}
