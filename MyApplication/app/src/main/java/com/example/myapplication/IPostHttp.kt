package com.example.myapplication

class IPostHttp(
    val id: Int,
    var userId: Any,    // A veces los servidores devuelven o Int o String
    val title: String,
    var body: String
) {
    init {
        if (userId is String)
            userId = (userId as String).toInt()
        if (userId is Int)
            userId = userId
    }
}