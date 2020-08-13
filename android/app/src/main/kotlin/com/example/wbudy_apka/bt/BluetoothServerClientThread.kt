package com.example.wbudy_apka.bt

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.example.wbudy_apka.event.EventRepository
import java.io.IOException
import java.lang.Exception
import java.math.BigInteger

class BluetoothServerClientThread(private var context: Context, private var socket: BluetoothSocket): Thread() {
    private var eventRepository: EventRepository = EventRepository(context)
    private var shouldLoop: Boolean = true;
    override fun run() {
        Log.i("BT Server","Started ...")
        sendMessage(BluetoothMessageFactory.startMessage)
        while(shouldLoop && socket.isConnected) {
            //Log.i("BT Server Thread","Loop ...")
            try {
                val recivedMsg = recvMessage();
                Log.i("BT Server Thread","header: ${BluetoothMessageType.messageTypeToString(recivedMsg.getHeader())}")
                when(recivedMsg.getHeader()) {
                    BluetoothMessageType.START -> {
                        sendMessage(BluetoothMessageFactory.okMessage)
                    }
                    BluetoothMessageType.END -> {
                        sendMessage(BluetoothMessageFactory.stopMessage)
                        shouldLoop = false;
                        socket.close()
                    }
                    BluetoothMessageType.PREV -> {
                        var payload = recivedMsg.getPayload()
                        var id = BigInteger(payload).toLong()
                        Log.i("payload id", id.toString())
                        sendMessage(BluetoothMessageFactory.okMessage(eventRepository.getPrevEventById(id).toString().toByteArray(Bluetooth.charset)))
                    }
                    BluetoothMessageType.NEXT -> {
                        var payload = recivedMsg.getPayload()
                        var id = BigInteger(payload).toLong()
                        Log.i("payload id", id.toString())
                        sendMessage(BluetoothMessageFactory.okMessage(eventRepository.getNextEventById(id).toString().toByteArray(Bluetooth.charset)))
                    }
                    BluetoothMessageType.LAST -> {
                        sendMessage(BluetoothMessageFactory.okMessage(eventRepository.getLastEvent().toString().toByteArray(Bluetooth.charset)))
                    }
                    else -> {
                        Log.i("BT Server","unimplemented: $recivedMsg")
                        sendMessage(BluetoothMessageFactory.unimplementedMessage)
                    }
                }
            }
            finally {
                if(!socket.isConnected)
                {
                    socket.close()
                    shouldLoop = false;
                }
            }
        }
        try {
            socket.close()
        }
        catch (e: IOException) { }
        Log.i("BT Server","Stoped ...")
    }
    fun sendMessage(msg: BluetoothMessageFactory.BluetoothMessage) {
        socket.outputStream.write(msg.msg)
        Log.i("BT Server", "sent $msg")
    }
    fun recvMessage(): BluetoothMessageFactory.BluetoothMessage {
        var header: ByteArray = ByteArray(1,init = { t -> BluetoothMessageType.FAKE_MESSAGE})
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
        var payload: ByteArray = ByteArray(payloadSize,init = { t -> 0x00 })
        socket.inputStream.read(payload)
        val msg = header+payloadSizeBytes+payload
        android.util.Log.i("BT Server", "recived $msg")
        return BluetoothMessageFactory.BluetoothMessage(msg)
    }
}