package com.example.simple_chatapp.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class MyMessage:Serializable {
    var id:String? = null
    var fromUid:String? = null
    var toUid:String? = null
    var text:String? = null
    var date:String? = null


    constructor(id: String?, fromUid: String?, toUid: String?, text: String?, date: String = SimpleDateFormat("HH:mm").format(Date())) {
        this.id = id
        this.fromUid = fromUid
        this.toUid = toUid
        this.text = text
        this.date = date
    }

    constructor()


}