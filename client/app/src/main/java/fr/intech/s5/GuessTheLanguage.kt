package fr.intech.s5

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class GuessTheLanguage: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val playButton : Button = findViewById(R.id.playButton)
        val rulesButton : Button = findViewById(R.id.rulesButton)
        var pseudo : EditText = findViewById(R.id.pseudo)

        playButton.setOnClickListener {
            if(!pseudo.text.isEmpty()) {
                launchGame()
            }
        }
        rulesButton.setOnClickListener {
            launchRules()
        }
    }

    fun launchGame() {
        val intent = Intent(this, Game::class.java)
        startActivity(intent)
    }

    fun launchRules() {
        val intent = Intent(this, Rules::class.java)
        startActivity(intent)
    }
}