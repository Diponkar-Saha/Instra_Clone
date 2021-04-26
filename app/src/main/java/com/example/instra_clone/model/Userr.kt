package com.example.instra_clone.model

class Userr
{

    private var email:String=""
    private var name:String=""
    private var uid:String=""

    constructor()

    constructor(email: String, name: String, uid: String) {
        this.email = email
        this.name = name
        this.uid = uid
    }

    fun getEmail(): String{
        return email
    }
    fun setEmail(email: String){
        this.email=email
    }

    fun getName(): String{
        return name
    }
    fun setName(name: String){
        this.name = name
    }

    fun getUid(): String{
        return uid
    }
    fun setUid(uid: String){
        this.uid = uid
    }
}