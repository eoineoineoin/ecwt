package es.eoinrul.ecwt

import android.content.ContentProvider
import android.content.Context
import android.media.*
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import es.eoinrul.ecwt.R

// Activity that only echos any inputs typed on a keyboard
class SounderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sounder)

        //val sounderMode = intent.getStringExtra(SOUNDER_MODE);

        mTextViewTest = findViewById<TextView>(R.id.keyedText);

        initSoundPlayer()
        onKeyUp(KeyEvent.KEYCODE_E, KeyEvent(0,0))
    }

    override fun onResume() {
        super.onResume()
        initSoundPlayer()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val sequence = KeycodeToSoundSequence(keyCode);

        mTextViewTest?.text = sequenceToString(sequence);

        mSoundPlayer?.enqueue(sequence);
        return true;
    }

    override fun onPause() {
        super.onPause();

        mSoundPlayer?.quit()
        mSoundPlayer = null
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

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings()
        generatorSettings.initFromPreferences(this)

        mSoundPlayer = DitDahSoundStream(generatorSettings)
    }

    private var mTextViewTest : TextView? = null;
    private var mMediaPlayer : MediaPlayer? = null;

    private var mSoundPlayer : DitDahSoundStream? = null;
}
