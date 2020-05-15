package com.example.ecwt

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//const val PREFERENCES_FILE = "es.eoinrul.ecwt.PREFERENCES"

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cw_settings)

        // Sounds: WPM; Farnsworth; (character speed versus word speed) Tone freq
        // Koch: Lesson duration; word size
        //var preferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
    }
}
