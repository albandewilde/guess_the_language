package fr.intech.s5

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.beust.klaxon.Klaxon
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.*
import utils.UserInfos
import java.io.IOException
import okhttp3.OkHttpClient



class Game: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pseudo = intent.getStringExtra("Pseudo")
        setContentView(R.layout.game_activity)

        var displayPseudo : TextView = findViewById(R.id.pseudoDisplay)
        var displayLevel : TextView = findViewById(R.id.levelDisplay)
        var displayPoints : TextView = findViewById(R.id.pointsDisplay)

        val ipServer = utils.IP_SERVER
        val url = ipServer + "subscribe_or_connect/$pseudo"

        val requestBody = MultipartBody.Builder()
            .addFormDataPart("pseudo", pseudo)
            .build()

        var httpClient = OkHttpClient()

        var request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()

        var resp = httpClient.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val user = Klaxon().parse<UserInfos>(response.body()!!.string())
                Log.i("erreur", user.toString())
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        if(!pseudo.isNullOrEmpty()) {
            displayPseudo.setText("Pseudo : " + pseudo)
            println(resp.toString())
        }
    }
}