package com.example.simple_chatapp.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date

class User: Serializable {
    var id: String? = null
    var name: String? = null
    var date:String? = SimpleDateFormat("HH:mm").format(Date())
//    var isOnline:Boolean? = null
    var imageLink: String? = null

    constructor(id: String?, name: String?, imageLink: String?) {
        this.id = id
        this.name = name
        this.imageLink = imageLink
    }

    constructor()
}