package com.example.wbudy_apka.location

class NmeaState() {
    var GPGSVSatelitesInView: MutableList<SVInView> = ArrayList(0)
    var GPGSVMessageCounter: Int = 0
    var GPGSVTotalMessageCounter: Int = 0

    var GLGSVSatelitesInView: MutableList<SVInView> = ArrayList(0)
    var GLGSVMessageCounter: Int = 0
    var GLGSVTotalMessageCounter: Int = 0

    fun getGPGSVNextMessageNumber(): Int {
        if(GPGSVMessageCounter == GPGSVTotalMessageCounter)
            return 1
        return GPGSVMessageCounter+1
    }
    fun getGLGSVNextMessageNumber(): Int {
        if(GLGSVMessageCounter == GLGSVTotalMessageCounter)
            return 1
        return GLGSVMessageCounter+1
    }

    fun getTotalMessageCounter(): Int {
        return GPGSVTotalMessageCounter+GLGSVTotalMessageCounter
    }
    fun getMessageCounter(): Int {
        return GPGSVMessageCounter+GLGSVMessageCounter
    }
    fun getAllSVInView(): List<SVInView> {
        var list: ArrayList<SVInView> = ArrayList()
        list.addAll(GPGSVSatelitesInView)
        list.addAll(GLGSVSatelitesInView)
        return list
    }

    override fun toString(): String {
        return "NmeaState(GPGSVMessageCounter=$GPGSVMessageCounter, GPGSVTotalMessageCounter=$GPGSVTotalMessageCounter, GLGSVMessageCounter=$GLGSVMessageCounter, GLGSVTotalMessageCounter=$GLGSVTotalMessageCounter)"
    }
}