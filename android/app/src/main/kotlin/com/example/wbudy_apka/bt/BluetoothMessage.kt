package com.example.wbudy_apka.bt

import android.util.Log

object BluetoothMessageType {
    const val START: Byte = 0x01
    const val END: Byte = 0x04
    const val OK: Byte = 0x06
    const val LAST: Byte = 0x10
    const val PREV: Byte = 0x11
    const val NEXT: Byte = 0x12
    const val UNIMPLEMENTED: Byte = 0x7F
    val allMessageTypes: Array<Byte> = arrayOf<Byte>(START,END,OK,LAST,PREV,NEXT,UNIMPLEMENTED)
    const val FAKE_MESSAGE: Byte = 0x00
    fun messageTypeToString(type: Byte): String {
        when(type) {
            START -> return "START"
            END -> return "END"
            OK -> return "OK"
            LAST -> return "LAST"
            PREV -> return "PREV"
            NEXT -> return "NEXT"
            UNIMPLEMENTED -> return "UNIMPLEMENTED"
            FAKE_MESSAGE -> return "FAKE_MESSAGE"
            else -> {
                return type.toString()
            }
        }
    }
}
object BluetoothMessageFactory {

    fun shortToByteArray(number: Int): ByteArray {
        val bytes = ByteArray(2)
        val part0 = number%256
        val part1 = (number - part0)/256
        bytes.set(0,part0.toByte())
        bytes.set(1,part1.toByte())
        return bytes
    }

    @ExperimentalUnsignedTypes
    fun byteArraytoShort(bytes: ByteArray): Int {
        var part0 = bytes.get(0).toUByte()
        var part1 = bytes.get(1).toUByte()
        val int = part0 + (part1 * 256u);
        return int.toInt()
    }

    private fun messageWithPayload(header: Byte,payload: ByteArray): ByteArray {
        val payloadSize: Int = payload.size
        var byteArray = ByteArray(3+payloadSize)
        val payloadSizeAsBytesArray = shortToByteArray(payloadSize)
        byteArray.set(0,header)
        byteArray.set(1,payloadSizeAsBytesArray.get(0))
        byteArray.set(2,payloadSizeAsBytesArray.get(1))
        for(x in 3 until byteArray.size) {
            byteArray.set(x,payload.get(x-3))
        }
        return byteArray;
    }
    private fun messageWithoutPayload(header: Byte): ByteArray {
        var byteArray = ByteArray(3)
        val payloadSize: Int = 0
        val payloadSizeAsBytesArray = shortToByteArray(payloadSize)
        byteArray.set(0,header)
        byteArray.set(1,payloadSizeAsBytesArray.get(0))
        byteArray.set(2,payloadSizeAsBytesArray.get(1))
        return byteArray;
    }

    class  BluetoothMessage(var msg: ByteArray) {
        fun getHeader(): Byte {
            val header =  msg.get(0)
            if(BluetoothMessageType.allMessageTypes.contains(header)){
                return header
            }
            else {
                return BluetoothMessageType.FAKE_MESSAGE
            }
        }
        fun getPayloadSize(): Int {
            val bytes = ByteArray(2)
            bytes.set(0,msg.get(1))
            bytes.set(1,msg.get(2))
            return byteArraytoShort(bytes)
        }
        fun getPayload(): ByteArray {
            val payload = msg.copyOfRange(3,msg.lastIndex+1)
            if(payload.size != getPayloadSize()) {
                Log.i("getPayload", "getPayload().size = ${payload.size} getPayloadSize() = ${getPayloadSize()}")
            }
            return payload
        }

        override fun toString(): String {
            return "Message( header: ${BluetoothMessageType.messageTypeToString(getHeader())}, payload size: ${getPayloadSize()} ) ${msg.contentToString()}"
        }
    }

    fun createMessageFromByteArray(msg: ByteArray): BluetoothMessage {
        return BluetoothMessage(msg)
    }
    val startMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.START)) }
    val stopMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.END)) }
    val okMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.OK)) }
    val lastMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.LAST)) }
    val fakeMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.FAKE_MESSAGE)) }
    val unimplementedMessage: BluetoothMessage get() { return BluetoothMessage(messageWithoutPayload(BluetoothMessageType.UNIMPLEMENTED)) }
    fun prevMessage(id: Long): BluetoothMessage {
        return BluetoothMessage(messageWithPayload(BluetoothMessageType.PREV,id.toBigInteger().toByteArray()))
    }
    fun nextMessage(id: Long): BluetoothMessage {
        return BluetoothMessage(messageWithPayload(BluetoothMessageType.NEXT,id.toBigInteger().toByteArray()))
    }
    fun okMessage(payload: ByteArray): BluetoothMessage {
        return BluetoothMessage(messageWithPayload(BluetoothMessageType.OK,payload))
    }

}
