package es.eoinrul.ecwt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*
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
        mEnteredTextView = findViewById<EditText>(R.id.enteredText);
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
        mLessonStarted = false
    }

    override fun onPause() {
        super.onPause();
        mSoundPlayer?.quit()
        mSoundPlayer = null
        mLessonStarted = false
    }

    override fun streamFinished(stream: DitDahSoundStream) {
        if(!mLessonStarted) {
            // Don't do anything if we've stopped the activity
            return
        }

        // Wait for a bit before the lesson ends to give user time to enter the last characters
        var secondsPauseBeforeLessonEnd : Long = 2
        var activityContext : Context = this
        Timer().schedule(object : TimerTask() {
            override fun run() {
                // The lesson text has an extra space at the end, which we don't want to grade
                var lessonText = mLessonText.trim()
                // The input text has a leading space that we don't want to grade
                var inputText = mEnteredTextView.text.toString()

                val intent = Intent(activityContext, TrainingResultsActivity::class.java).apply {
                    putExtra(TRAINING_ANSWER, lessonText)
                    putExtra(TRAINING_COPIED, inputText)
                    putExtra(TRAINING_ALPHABET, mAlphabet) // For a "retry" button
                }
                startActivity(intent);
            }
        }, secondsPauseBeforeLessonEnd * 1000)
    }

    fun onStartTrainingClicked(view : View) {
        if(mLessonStarted)
            return

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

        mLessonStarted = true
        mLessonText = lessonText

        // Insert a space into the editable, to clear the hint
        mEnteredTextView.text.append(" ")

        // Pause before starting to let keyboard appear, and user get ready to type
        var secondsPauseBeforeLessonStart : Long = 2
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if(mLessonStarted) {
                    mSoundPlayer!!.enqueue(StringToSoundSequence(lessonText))
                }
            }
        }, secondsPauseBeforeLessonStart * 1000)
    }

    private var mAlphabet : String = ""
    private var mSoundPlayer : DitDahSoundStream? = null
    private lateinit var mEnteredTextView : EditText
    private var mLessonStarted : Boolean = false
    private var mLessonText : String = ""

    private var mLessonStartDelayTimer : Timer = Timer()
}
