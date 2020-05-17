package es.eoinrul.ecwt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlin.random.Random

const val TRAINING_ANSWER = "es.eoinrul.ecwt.TRAINING_ANSWER"
const val TRAINING_COPIED = "es.eoinrul.ecwt.TRAINING_COPIED"

class TrainingActivity : AppCompatActivity(),
    DitDahSoundStream.StreamNotificationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traning)

        initSoundPlayer()

        mAlphabet = intent.getStringExtra(TRAINING_ALPHABET)!!;
        mEnteredTextView = findViewById<TextView>(R.id.enteredText);
    }

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings()
        generatorSettings.initFromPreferences(this)

        mSoundPlayer = DitDahSoundStream(generatorSettings)
        mSoundPlayer!!.streamNotificationListener = this
    }

    override fun onResume() {
        super.onResume()
        initSoundPlayer()
        mEnteredTextView.text = "Touch to Start" // TODO Reset properly, somehow
        mLessonStarted = false
    }

    override fun onPause() {
        super.onPause();
        mSoundPlayer?.quit()
        mSoundPlayer = null

        if(mLessonStarted) {
            // Shut down the soft keyboard
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }

        mLessonStarted = false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        // TODO This is a pretty lame input-method implementation. Do this properly.
        if(!mLessonStarted)
            return true

        val previousText : String = mEnteredTextView.text.toString()

        if(event.keyCode == KeyEvent.KEYCODE_DEL) {
            if(previousText.isNotEmpty()) {
                mEnteredTextView.text = previousText.substring(0, previousText.length - 1)
            }
        }
        else {
            val enteredChar : Char = event.unicodeChar.toChar()
            mEnteredTextView.text = previousText + enteredChar.toString()
        }

        return true
    }

    override fun streamFinished(stream: DitDahSoundStream) {
        if(!mLessonStarted) {
            // Don't do anything if we've stopped the activity
            return
        }

        // The lesson text has an extra space at the end, which we don't want to grade
        var lessonText = mLessonText.trim()
        // The input text has a leading space that we don't want to grade
        var inputText = mEnteredTextView.text.trim()

        val intent = Intent(this, TrainingResultsActivity::class.java).apply {
            putExtra(TRAINING_ANSWER, lessonText)
            putExtra(TRAINING_COPIED, inputText)
            putExtra(TRAINING_ALPHABET, mAlphabet) // For a "retry" button
        }
        startActivity(intent);
    }

    fun onStartTrainingClicked(view : View) {
        if(mLessonStarted)
            return

        mEnteredTextView.text = " "

        // Bring up the soft keyboard
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        val generatorSettings = DitDahGeneratorSettings()

        val lessonLengthInMinutes = 1 // TODO These should be configurable from the settings window
        val wordSize = 5

        var sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val quickTestingSwitchEnabled = sharedPrefs.getBoolean("switch_preference_1", false)
        val numberOfWords = if(!quickTestingSwitchEnabled) {
            generatorSettings.farnsworthWordsPerMinute * lessonLengthInMinutes
        } else { 2 } // This is just for quicker testing; remove eventually

        var lessonText = String()
        var rng = Random.Default
        for(i in 0 until numberOfWords) {
            for(c in 0 until wordSize) {
                val randomLetterIndex = rng.nextInt(0, mAlphabet.length)
                lessonText += mAlphabet.substring(randomLetterIndex, randomLetterIndex + 1)
            }
            lessonText += " "
        }

        mSoundPlayer!!.enqueue(StringToSoundSequence(lessonText))

        mLessonStarted = true
        mLessonText = lessonText
    }

    private var mAlphabet : String = ""
    private var mSoundPlayer : DitDahSoundStream? = null
    private lateinit var mEnteredTextView : TextView
    private var mLessonStarted : Boolean = false
    private var mLessonText : String = ""
}
