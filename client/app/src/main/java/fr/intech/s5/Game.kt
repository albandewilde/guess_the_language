package fr.intech.s5

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.beust.klaxon.Klaxon
import okhttp3.*
import utils.UserInfos
import java.io.IOException
import okhttp3.OkHttpClient
import utils.QuestionsList
import com.google.gson.Gson

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
                //Log.i("ntm", response.body()!!.string())
                var gson = Gson()
                var user = gson?.fromJson(response.body()!!.string(), UserInfos::class.java)

                val logosRequestUrl = ipServer + "get_more_logo/${user!!.level}"

                var logosRequest = Request.Builder()
                    .url(logosRequestUrl)
                    .get()
                    .build()

                var response = httpClient.newCall(logosRequest).enqueue(object: Callback {
                    override fun onResponse(call: Call, response: Response) {
                        var gson = Gson()
                        var questions = gson?.fromJson(response.body()!!.string(), QuestionsList::class.java)

                        user.currentQuestion = 0

                        runOnUiThread(Runnable {
                            updateUserInfos(user)
                            displayQuestion(questions, user)
                        })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    fun updateUserInfos(user: UserInfos?) {
        var displayPseudo: TextView = findViewById(R.id.pseudoDisplay)
        var displayLevel: TextView = findViewById(R.id.levelDisplay)
        var displayPoints: TextView = findViewById(R.id.pointsDisplay)

        var pseudo = "Pseudo : " + user?.pseudo
        var level = "Level : " + user?.level
        var points = "Points : " + user?.points

        displayPseudo.setText(pseudo)
        displayLevel.setText(level)
        displayPoints.setText(points)
    }

    fun displayQuestion(questions: QuestionsList?, user: UserInfos) {
        var currentIndex = user.currentQuestion

        var logo: ImageView = findViewById(R.id.langLogo)
        var bitmap: Bitmap = decodeImage(questions!!.content[currentIndex].path)

        logo.setImageBitmap(bitmap)

        var answer0: Button = findViewById(R.id.nb_0)
        var answer1: Button = findViewById(R.id.nb_1)
        var answer2: Button = findViewById(R.id.nb_2)
        var answer3: Button = findViewById(R.id.nb_3)

        var answers = mutableListOf(answer0, answer1, answer2, answer3)

        for((index, button) in answers.withIndex()) {
            button.setText(questions!!.content[currentIndex].choices[index])
            button.setBackgroundColor(Color.WHITE)

            if(questions!!.content[currentIndex].response_idx == index) {
                button.setOnClickListener(
                    View.OnClickListener {
                        goodAnswer(user, questions)
                    }
                )
            } else {
                button.setOnClickListener(
                    View.OnClickListener {
                        wrongAnswer(user, button, questions)
                    }
                )
            }
        }
    }

    fun decodeImage(data: String): Bitmap{
        val decodedString = Base64.decode(data, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun goodAnswer(user: UserInfos, questions: QuestionsList) {
        user.currentQuestion += 1
        user.points += 5

        var httpClient = OkHttpClient()
        val nextLevelRequest = utils.IP_SERVER + "next_level/${user!!.pseudo}/${user!!.level}"

        val requestBody = MultipartBody.Builder()
            .addFormDataPart("pseudo", user.pseudo)
            .addFormDataPart("level", user.level.toString())
            .build()

        var logosRequest = Request.Builder()
            .url(nextLevelRequest)
            .post(requestBody)
            .build()

        var response = httpClient.newCall(logosRequest).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                //it is a update request so we won't get any response
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })

        nextQuestions(questions, user)
    }

    fun wrongAnswer(user: UserInfos, button: Button, questions: QuestionsList) {
        user.points += 3
        button.setBackgroundColor(Color.RED)
        displayQuestion(questions, user)
    }

    fun nextQuestions(questions: QuestionsList, user: UserInfos) {
        var idx = user.currentQuestion
        var nbQuestions = questions.content.size

        if(idx == nbQuestions) {

            user.currentQuestion = 0

            val logosRequestUrl = utils.IP_SERVER + "get_more_logo/${user!!.level}"

            var httpClient = OkHttpClient()

            var logosRequest = Request.Builder()
                .url(logosRequestUrl)
                .get()
                .build()

            var response = httpClient.newCall(logosRequest).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    val questions = Klaxon().parse<QuestionsList>(response.body()!!.string())
                    displayQuestion(questions, user)
                }

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
            })
        } else {
            displayQuestion(questions, user)
        }
    }
}
