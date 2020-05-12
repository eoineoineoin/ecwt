package com.example.ecwt

import android.media.*
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        //val sounderMode = intent.getStringExtra(SOUNDER_MODE);

        mTextViewTest = findViewById<TextView>(R.id.TestEnteredText);

        mSoundPlayer = DitDahSoundStream(DitDahGeneratorSettings())
        onKeyUp(KeyEvent.KEYCODE_E, KeyEvent(0,0))
    }

    fun startTraining(view: View) {
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val sequence = KeycodeToSoundSequence(keyCode);

        mTextViewTest?.text = sequenceToString(sequence);

        mSoundPlayer?.enqueue(sequence);
        return true;
    }

    override fun onPause() {
        super.onPause();
    }


    private fun sequenceToString(sequence: List<SoundTypes>) : String {
        var ret = String()
        for(symbol in sequence) {
            ret += when(symbol) {
                SoundTypes.DIT -> "·"
                SoundTypes.DAH -> "-"
                else -> " "
            }
        }

        return ret
    }

    private var mTextViewTest : TextView? = null;
    private var mMediaPlayer : MediaPlayer? = null;

    private var mSoundPlayer : DitDahSoundStream? = null;
}
