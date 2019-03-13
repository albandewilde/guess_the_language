package fr.intech.s5

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.io.*
import us.feras.mdv.MarkdownView

class Rules: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rules_activity)

        var rulesText : MarkdownView = findViewById(R.id.rulesText)
        val file : InputStream = resources.openRawResource(R.raw.regles)
        var rules = ""

        try {
            rules = file.bufferedReader().use { it.readText() }
            rulesText.loadMarkdown(rules)

        } catch(e : Exception) {
            e.printStackTrace()
        }
    }
}