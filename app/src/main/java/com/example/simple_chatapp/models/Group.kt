package com.example.simple_chatapp.models

import java.io.Serializable

class Group:Serializable {
    var name: String? = null
    var uid:String? = null

    constructor()
    constructor(name: String?) {
        this.name = name
    }

}