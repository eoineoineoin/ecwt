package com.example.ecwt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

const val SOUNDER_MODE = "es.eoinrul.ecwt.SOUNDER_MODE"
const val SOUNDER_MODE_USER_INPUT = "es.eoinrul.ecwt.SOUNDER_MODE.USER_INPUT";

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openSettings(view: View) {
        val intent = Intent(this, SettingsActivity::class.java);
        startActivity(intent);
    }

    fun openSounder(view: View) {
        val intent = Intent(this, SounderActivity::class.java).apply {
            putExtra(SOUNDER_MODE, SOUNDER_MODE_USER_INPUT)
        }
        startActivity(intent);
    }

    fun openKochLevelSelect(view :View) {
        val intent = Intent(this, LevelSelectActivity::class.java).apply {
        }
        startActivity(intent)
    }
}
