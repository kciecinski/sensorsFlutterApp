package com.example.wbudy_apka.bt

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.widget.Toast
import com.example.wbudy_apka.event.Event
import io.flutter.Log
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset

class BluetoothClient(private var context: Context) {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    val pairedDevices: Set<BluetoothDevice>
        get() { return bluetoothAdapter.bondedDevices; }

    fun getDeviceByAddres(addres: String): BluetoothDevice? {
        var device = pairedDevices.find { t -> t.address.toString() == addres };
        return device;
    }

    fun sendMessage(socket: BluetoothSocket,msg: BluetoothMessageFactory.BluetoothMessage) {
        socket.outputStream.write(msg.msg)
        android.util.Log.i("BT Client", "sent $msg")
    }
    fun recvMessage(socket: BluetoothSocket): BluetoothMessageFactory.BluetoothMessage {
        var header: ByteArray = ByteArray(1,init = { _ -> BluetoothMessageType.FAKE_MESSAGE})
        socket.inputStream.read(header)
        var payloadSizeBytes = BluetoothMessageFactory.shortToByteArray(0)
        if(!BluetoothMessageType.allMessageTypes.contains<Byte>(header.get(0))) {
            var msg = ByteArray(3)
            msg.set(0,header.get(0))
            msg.set(1,payloadSizeBytes.get(0))
            msg.set(2,payloadSizeBytes.get(1))
            return BluetoothMessageFactory.BluetoothMessage(msg)
        }
        socket.inputStream.read(payloadSizeBytes)
        val payloadSize = BluetoothMessageFactory.byteArraytoShort(payloadSizeBytes)
        Log.i("recv on Client","payloadSize: $payloadSize as bytes: ${payloadSizeBytes.contentToString()}")
        var payload: ByteArray = ByteArray(payloadSize,init = { _ -> 0x00 })
        socket.inputStream.read(payload)
        val msg = header+payloadSizeBytes+payload
        android.util.Log.i("BT Client", "recived $msg")
        return BluetoothMessageFactory.BluetoothMessage(msg)
    }

    private fun connectTo(device: BluetoothDevice): BluetoothSocket? {
        var socket: BluetoothSocket? = null
        //Create new socket
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(Bluetooth.uuid)
        } catch (e: IOException) {
            Log.i("BT Client Error 1.1",e.localizedMessage)
            e.printStackTrace()
            return null;
        }
        if(socket == null) {
            Log.i("BT Client 1.2","null socket")
            return null;
        }
        try{
            socket.connectIfNotConnected()
            //sendMessage(socket,BluetoothMessageFactory.startMessage)
            for(i in 0 until 10) {
                var msg = recvMessage(socket)
                if(BluetoothMessageType.allMessageTypes.contains(msg.getHeader())) {
                    return socket
                }
            }
        }
        catch (e: IOException) {
            Log.i("BT Client Error 1.3",e.localizedMessage)
            e.printStackTrace()
        }
        //Second try to create socket
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(Bluetooth.uuid)
        } catch (e: IOException) {
            Log.i("BT Client Error 2.1",e.localizedMessage)
            e.printStackTrace()
            return null;
        }
        if(socket == null) {
            Log.i("BT Client 2.2","Null socket in connectTO")
            return null;
        }
        try{
            socket.connectIfNotConnected()
            sendMessage(socket,BluetoothMessageFactory.startMessage)
            for(i in 0 until 10) {
                var msg = recvMessage(socket)
                if(BluetoothMessageType.allMessageTypes.contains(msg.getHeader())) {
                    return socket
                }
            }
        }
        catch (e: IOException) {
            Log.i("BT Client Error 2.3",e.localizedMessage)
            e.printStackTrace()
        }

        return null
    }

    private fun timeoutAfterDisconnect() {
        Thread.sleep(2000)
    }

    fun getEvent(device: BluetoothDevice,initMessage: BluetoothMessageFactory.BluetoothMessage,callbackRecivedMessage: (recivedMsg: BluetoothMessageFactory.BluetoothMessage,socket: BluetoothSocket) -> String?): Event {
        var eventJsonString = "";
        var socket: BluetoothSocket? = null
        try {
            socket = connectTo(device);
            if(socket == null) return Event.invalidEvent
            if(!socket.isConnected) {
                socket.connectIfNotConnected()
            }
            sendMessage(socket,initMessage);
            var shouldLoop = true;
            while (shouldLoop && socket.isConnected) {
                var recivedMsg = recvMessage(socket)
                val ret = callbackRecivedMessage(recivedMsg,socket)
                if(ret != null) {
                    eventJsonString = ret
                }
            }
        }
        catch (e: Exception) {
            Log.i("BT Client getLastEvent Error",e.localizedMessage)
            e.printStackTrace()
        }
        finally {
            try {
                if (socket != null) {
                    socket.close()
                }
            }
            catch (e: IOException) { }
        }
        var event = Event.invalidEvent
        if(eventJsonString != "") {
            event = Event.fromJsonString(eventJsonString)
            Log.i("BT Client",eventJsonString.toString())
            Toast.makeText(context,"BT Client"+ eventJsonString.toString(),Toast.LENGTH_LONG).show()
        }
        timeoutAfterDisconnect()
        return event
    }

    fun getNextEvent(device: BluetoothDevice, id: Long): Event {
        val callback = fun(recivedMsg: BluetoothMessageFactory.BluetoothMessage, socket: BluetoothSocket): String? {
            var ret: String? = null
            when(recivedMsg.getHeader()) {
                BluetoothMessageType.OK -> {
                    Log.i("BT Client","recived event ${recivedMsg.msg.toString(Bluetooth.charset)}")
                    val payloadBytes = recivedMsg.getPayload()
                    ret = payloadBytes.toString(Bluetooth.charset)
                    sendMessage(socket,BluetoothMessageFactory.stopMessage)
                    socket.close()
                }
                else -> {
                    sendMessage(socket,BluetoothMessageFactory.nextMessage(id))
                }
            }
            return ret;
        }
        return getEvent(device,BluetoothMessageFactory.nextMessage(id),callback)
    }

    fun getPrevEvent(device: BluetoothDevice, id: Long): Event {
        val callback = fun(recivedMsg: BluetoothMessageFactory.BluetoothMessage, socket: BluetoothSocket): String? {
            var ret: String? = null
            when(recivedMsg.getHeader()) {
                BluetoothMessageType.OK -> {
                    Log.i("BT Client","recived event ${recivedMsg.msg.toString(Bluetooth.charset)}")
                    val payloadBytes = recivedMsg.getPayload()
                    ret = payloadBytes.toString(Bluetooth.charset)
                    sendMessage(socket,BluetoothMessageFactory.stopMessage)
                    socket.close()
                }
                else -> {
                    sendMessage(socket,BluetoothMessageFactory.prevMessage(id))
                }
            }
            return ret;
        }
        return getEvent(device,BluetoothMessageFactory.prevMessage(id),callback)
    }

    fun getLastEvent(device: BluetoothDevice): Event {
        val callback = fun(recivedMsg: BluetoothMessageFactory.BluetoothMessage, socket: BluetoothSocket): String? {
            var ret: String? = null
            when(recivedMsg.getHeader()) {
                BluetoothMessageType.OK -> {
                    Log.i("BT Client","recived event ${recivedMsg.msg.toString(Bluetooth.charset)}")
                    val payloadBytes = recivedMsg.getPayload()
                    ret = payloadBytes.toString(Bluetooth.charset)
                    sendMessage(socket,BluetoothMessageFactory.stopMessage)
                    socket.close()
                }
                else -> {
                    sendMessage(socket,BluetoothMessageFactory.lastMessage)
                }
            }
            return ret;
        }
        return getEvent(device,BluetoothMessageFactory.lastMessage,callback)
    }
}