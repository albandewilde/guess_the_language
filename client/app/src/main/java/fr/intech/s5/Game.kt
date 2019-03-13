package fr.intech.s5

import android.app.Activity
import android.os.Bundle

class Game: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)
    }
}