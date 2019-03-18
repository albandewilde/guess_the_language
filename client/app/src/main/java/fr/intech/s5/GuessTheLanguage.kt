package fr.intech.s5

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.security.NetworkSecurityPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.main_activity.*

class GuessTheLanguage: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val playButton : Button = findViewById(R.id.playButton)
        val rulesButton : Button = findViewById(R.id.rulesButton)
        var pseudo : EditText = findViewById(R.id.pseudoInput)
        val insertPseudoText : TextView = findViewById(R.id.insertPseudo)

        playButton.setOnClickListener {
            if(!pseudo.text.isEmpty()) {
                launchGame()
            } else {
                Toast.makeText(this, "Incorrect Pseudo", Toast.LENGTH_LONG).show()
                insertPseudoText.setTextColor(Color.RED)
            }
        }
        rulesButton.setOnClickListener {
            launchRules()
        }
    }

    fun launchGame() {
        val intent = Intent(this, Game::class.java)
        intent.putExtra("Pseudo", pseudoInput.text.toString())
        startActivity(intent)
    }

    fun launchRules() {
        val intent = Intent(this, Rules::class.java)
        startActivity(intent)
    }
}