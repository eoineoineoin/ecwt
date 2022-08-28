package es.eoinrul.ecwt

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max


// Activity that only echos any inputs typed on a keyboard
class SounderActivity : AppCompatActivity(), DitDahSoundStream.StreamNotificationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sounder)

        //val sounderMode = intent.getStringExtra(SOUNDER_MODE);
        mPreviouslySentText = findViewById<TextView>(R.id.sentText)
        mCurrentlySendingText = findViewById<TextView>(R.id.keyedText)

        // This is a hidden EditText that's used to bring up the soft keyboard:
        mKeyboardInput = findViewById<EditText>(R.id.sounderInput)
        mKeyboardInput?.addTextChangedListener(mInputHandler)
        mKeyboardInput?.requestFocus()
        mKeyboardInput?.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(!mAutoSend && (actionId == EditorInfo.IME_ACTION_SEND ||
                        (actionId == EditorInfo.IME_NULL && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) ) ) {
                mUserTriggeredSend = true
                mKeyboardInput!!.isEnabled = false
                ensurePlaying()
            }
            mAutoSend
        }

        var autoSendCheck = findViewById<CheckBox>(R.id.autoSendCheck);
        autoSendCheck.setOnCheckedChangeListener { compoundButton, _ ->
            mAutoSend = compoundButton.isChecked
        }
        autoSendCheck.isChecked = mAutoSend

        initSoundPlayer()
    }

    override fun onResume() {
        super.onResume()
        initSoundPlayer()
    }

    override fun onPause() {
        super.onPause()

        mSoundPlayer?.quit()
        mSoundPlayer = null
    }

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings(this)
        mSoundPlayer = DitDahSoundStream(generatorSettings)
        mSoundPlayer!!.streamNotificationListener = this
        mSpaceDurationMs = (mSoundPlayer!!.durationOf(listOf(SoundTypes.LETTER_SPACE)) * 1000).toLong()
    }

    private var mAutoSend : Boolean = true
    private var mUserTriggeredSend : Boolean = false
    private var mPreviouslySentText : TextView? = null
    private var mCurrentlySendingText : TextView? = null
    private var mKeyboardInput : EditText? = null

    private var mSoundPlayer : DitDahSoundStream? = null
    // Flag to indicate if the sound player is waiting to send another symbol:
    private var mSoundAwaitingText : Boolean = true

    // Duration of a space with current sound gen. Used to add spaces
    // automatically, if user input is slow
    private var mSpaceDurationMs : Long = 0
    private var mTimeOfFirstSymbolWait : Long = 0

    // Utility to watch our EditText and handle any user input
    // This only seems to get triggered if we're using the software keyboard
    private val mInputHandler = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            ensurePlaying()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun ensurePlaying() {
        if(mSoundAwaitingText && mSoundPlayer != null) {
            symbolsExhausted(mSoundPlayer!!)
        }
    }

    override fun symbolsExhausted(stream: DitDahSoundStream) {
        if (!mSoundAwaitingText) {
            mTimeOfFirstSymbolWait = System.currentTimeMillis()
        }

        mSoundAwaitingText = true
        runOnUiThread {
            if( (mAutoSend || mUserTriggeredSend) &&
                mKeyboardInput?.text!!.isNotEmpty() && mSoundAwaitingText ) {
                mSoundAwaitingText = false
                // Extract and remove the first character
                val firstInput = mKeyboardInput!!.text!!.subSequence(0, 1).toString()
                mKeyboardInput!!.text = mKeyboardInput!!.text.replace(0, 1, "")
                // Move text entry cursor to the end
                mKeyboardInput!!.setSelection(mKeyboardInput!!.text.length)

                val sequence = StringToSoundSequence(firstInput.capitalize())

                val now = System.currentTimeMillis()
                val extraSpace = if(now - mTimeOfFirstSymbolWait > mSpaceDurationMs) {
                    " "
                } else {
                    ""
                }

                // Display the text:
                mCurrentlySendingText?.text = SequenceToString(sequence)
                val maxHistory = 20
                val trimFrom = max(0, mPreviouslySentText?.text!!.length - maxHistory
                        - extraSpace.length - firstInput.length)
                mPreviouslySentText?.text = mPreviouslySentText?.text!!.substring(trimFrom,
                    mPreviouslySentText?.text!!.length) + extraSpace + firstInput

                // Re-enable the send button if this was the last character to go
                if (mUserTriggeredSend && mKeyboardInput!!.text.isEmpty()) {
                    mKeyboardInput!!.isEnabled = true
                    mKeyboardInput!!.requestFocus()
                    mUserTriggeredSend = false
                }

                //  Then play the sound:
                mSoundPlayer?.enqueue(sequence)
            }
        }
    }
}
