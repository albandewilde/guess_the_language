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
import utils.QuestionsList


class Game: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pseudo = intent.getStringExtra("Pseudo")
        setContentView(R.layout.game_activity)

        val ipServer = utils.IP_SERVER

        val userRequestUrl = ipServer + "subscribe_or_connect/$pseudo"
        val requestBody = MultipartBody.Builder()
            .addFormDataPart("pseudo", pseudo)
            .build()

        var httpClient = OkHttpClient()

        var userRequest = Request.Builder()
            .url(userRequestUrl)
            .put(requestBody)
            .build()

        var resp = httpClient.newCall(userRequest).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val user = Klaxon().parse<UserInfos>(response.body()!!.string())
                Log.i("erreur", user.toString())

                val logosRequestUrl = ipServer + "get_more_logo/${user!!.level}"

                var logosRequest = Request.Builder()
                    .url(logosRequestUrl)
                    .get()
                    .build()

                var response = httpClient.newCall(logosRequest).enqueue(object: Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val questions = Klaxon().parse<QuestionsList>(response.body()!!.string())
                    }

                    override fun onFailure(call: Call, e: IOException) {

                    }
                })

                runOnUiThread(Runnable {
                    updateUserInfos(user)
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun updateUserInfos(user: UserInfos?) {
        var displayPseudo : TextView = findViewById(R.id.pseudoDisplay)
        var displayLevel : TextView = findViewById(R.id.levelDisplay)
        var displayPoints : TextView = findViewById(R.id.pointsDisplay)

        var pseudo = "Pseudo : " + user?.pseudo
        var level = "Level : " + user?.level
        var points = "Points : " + user?.points

        displayPseudo.setText(pseudo)
        displayLevel.setText(level)
        displayPoints.setText(points)
    }
}