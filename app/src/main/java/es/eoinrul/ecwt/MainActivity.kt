package es.eoinrul.ecwt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import es.eoinrul.ecwt.R

const val SOUNDER_MODE = "es.eoinrul.ecwt.SOUNDER_MODE"
const val SOUNDER_MODE_USER_INPUT = "es.eoinrul.ecwt.SOUNDER_MODE.USER_INPUT";

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java);
        startActivity(intent);
    }

    fun openSounder(view: View) {
        val intent = Intent(this, SounderActivity::class.java).apply {
            putExtra(
                SOUNDER_MODE,
                SOUNDER_MODE_USER_INPUT
            )
        }
        startActivity(intent);
    }

    fun openKochLevelSelect(view :View) {
        val intent = Intent(this, LevelSelectActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
