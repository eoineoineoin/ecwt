package es.eoinrul.ecwt

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


// Activity that only echos any inputs typed on a keyboard
class SounderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sounder)

        //val sounderMode = intent.getStringExtra(SOUNDER_MODE);

        mTextViewTest = findViewById<TextView>(R.id.keyedText);

        // This is a hidden EditText that's used to bring up the soft keyboard:
        mKeyboardInput = findViewById<EditText>(R.id.sounderInput);
        mKeyboardInput?.addTextChangedListener(mInputHandler)
        mKeyboardInput?.requestFocus();

        initSoundPlayer()
        onTextEntered("E"); // Display and sound something - this is arbitrary
    }

    // This will be called for connected USB/Bluetooth keyboards:
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val sequence = KeycodeToSoundSequence(keyCode)
        mTextViewTest?.text = SequenceToString(sequence)
        mSoundPlayer?.enqueue(sequence)
        mKeyboardInput?.setText("") // Should be empty due to physical keyboard
        return true;
    }

    override fun onResume() {
        super.onResume()
        initSoundPlayer()
    }

    override fun onPause() {
        super.onPause();

        mSoundPlayer?.quit()
        mSoundPlayer = null
    }

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings(this)
        mSoundPlayer = DitDahSoundStream(generatorSettings)
    }

    private fun onTextEntered(typedString : String) {
        val sequence = StringToSoundSequence(typedString);

        // Display the text:
        mTextViewTest?.text = SequenceToString(sequence);

        //  Then play the sound:
        mSoundPlayer?.enqueue(sequence);

        // Finally, clear the input box, as we don't need it anymore:
        mKeyboardInput?.setText("");
    }

    private var mTextViewTest : TextView? = null;
    private var mKeyboardInput : EditText? = null;
    private var mMediaPlayer : MediaPlayer? = null;

    private var mSoundPlayer : DitDahSoundStream? = null;

    // Utility to watch our EditText and handle any user input
    // This only seems to get triggered if we're using the software keyboard
    private val mInputHandler = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if(s == null) {
                return;
            }

            if(s.isNotEmpty()) {
                var typedString: String = s.toString().capitalize();
                onTextEntered(typedString)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}
