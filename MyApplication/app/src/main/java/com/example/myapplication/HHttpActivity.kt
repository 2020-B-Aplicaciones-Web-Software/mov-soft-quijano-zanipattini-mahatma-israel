package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

import com.github.kittinunf.result.Result

class HHttpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hhttp)

        metodoGet()
        metodoPost()
    }

    fun metodoGet(){
        val identificadorPost = 1
        val url = "https://jsonplaceholder.typicode.com/posts/" + identificadorPost.toString()
        url
            .httpGet()
            .responseString{ req, res, result ->
                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        Log.i("http-klaxon", "Error: ${error}")
                    }
                    is Result.Success -> {
                        val respuestaString = result.get()
                        Log.i("http-klaxon", "${respuestaString}")

                        // Un solo elemento
                        // https://jsonplaceholder.typicode.com/posts/1

                        // Varios elementos
                        // https://jsonplaceholder.typicode.com/posts/

                        val post = Klaxon()
                            .parse<IPostHttp>(respuestaString)
                            //.parseArray<IPostHttp>(getString)     // Para varios objetos
                        Log.i("http-klaxon", "${post?.body}")
                    }
                }

            }
    }


    fun metodoPost(){
        val parametros: List<Pair<String, *>> = listOf(
            "title" to "Titulo moviles",
            "body" to "descripcion moviles",
            "userId" to 1
        )

        val url = "https://jsonplaceholder.typicode.com/posts"
        url
            .httpPost(parametros)
            .responseString { req, res, result ->
                when (result) {
                    is Result.Failure -> {
                        val error = result.getException()
                        Log.i("http-klaxon", "${error}")
                    }
                    is Result.Success -> {
                        val postString = result.get()
                        Log.i("http-klaxon", "${postString}")

                        val post = Klaxon()
                            .parse<IPostHttp>(postString)
                        Log.i("http-klaxon", "${post?.title}")
                    }
                }
            }
    }

}