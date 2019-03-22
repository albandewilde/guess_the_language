package fr.intech.s5

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import com.google.gson.Gson
import okhttp3.*
import utils.EndGame
import utils.QuestionsList
import java.io.IOException

class End: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.end_activity)

        var picture: ImageView = findViewById(R.id.endLogo)

        var httpClient = OkHttpClient()

        val endRequestUrl = utils.IP_SERVER + "end_game"
        var request = Request.Builder()
            .url(endRequestUrl)
            .get()
            .build()

        var response = httpClient.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                var gson = Gson()
                var questions = gson?.fromJson(response.body()!!.string(), EndGame::class.java)

                var picDisplay = questions!!.content
                val decodedString = Base64.decode(picDisplay, Base64.DEFAULT)
                var endLogo = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                runOnUiThread(Runnable {
                    picture.setImageBitmap(endLogo)
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}