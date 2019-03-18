package fr.intech.s5

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class Game: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var pseudo = intent.getStringExtra("Pseudo")
        setContentView(R.layout.game_activity)

        var displayPseudo : TextView = findViewById(R.id.pseudoDisplay)
        var displayLevel : TextView = findViewById(R.id.levelDisplay)
        var displayPoints : TextView = findViewById(R.id.pointsDisplay)

        if(!pseudo.isNullOrEmpty()) {
            displayPseudo.setText("Pseudo : " + pseudo)
        }
    }
}