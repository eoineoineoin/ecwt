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

        var lessionIndex = intent.getIntExtra(TRAINING_LESSON_INDEX, 0)
        mAlphabet = KochLessonDefinitions.KochLesson(lessionIndex).getAlphabet()
        mEnteredTextView = findViewById<EditText>(R.id.enteredText);
    }

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings(this)
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

    override fun symbolsExhausted(stream: DitDahSoundStream) {
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
                var inputText = mEnteredTextView.text.toString().trim()

                var lessonIndex = intent.getIntExtra(TRAINING_LESSON_INDEX, 0)

                val intent = Intent(activityContext, TrainingResultsActivity::class.java).apply {
                    putExtra(TRAINING_ANSWER, lessonText)
                    putExtra(TRAINING_COPIED, inputText)
                    putExtra(TRAINING_LESSON_INDEX, lessonIndex) // For a "retry/next" button
                }
                startActivity(intent);
            }
        }, secondsPauseBeforeLessonEnd * 1000)
    }

    fun onStartTrainingClicked(view : View) {
        if(mLessonStarted)
            return

         val generatorSettings = DitDahGeneratorSettings()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val lessonLengthInMinutes = sharedPreferences.getInt(getString(R.string.setting_koch_lesson_length_key), 1)
        var minWordSize = 5
        var maxWordSize = 5
        if(sharedPreferences.getBoolean(getString(R.string.setting_koch_lesson_vary_word_size_key), false)) {
            minWordSize = 2
            maxWordSize = 6
        }

        var sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val shortLessonDebug = sharedPrefs.getBoolean("debug_override_lesson_length", false)
        val numberOfWords = if(shortLessonDebug) {
            // This makes it much faster to test functionality, if enabled in preferences
            2
        } else {
            // This is only an approximation
            generatorSettings.farnsworthWordsPerMinute * lessonLengthInMinutes
        }

        // If selected in preferences, modify the alphabet used for the lesson so that
        // the most recently added character is more likely to occur; this helps a lot
        // in later lessons, since you are tested on the new character more frequently
        var lessonAlphabet = mAlphabet
        val invCharacterChance = 7 // Feels about right. A bit less frequent than once per word
        if(sharedPrefs.getBoolean("koch_lesson_train_new", true)
            && mAlphabet.length > invCharacterChance) {
            var lastChar = mAlphabet.substring(mAlphabet.length - 1)
            lessonAlphabet = mAlphabet.repeat(invCharacterChance - 1)
            lessonAlphabet += lastChar.repeat(mAlphabet.length - invCharacterChance)
        }


        // Create the actual text used for the lesson
        var lessonText = String()
        var rng = Random.Default
        for(i in 0 until numberOfWords) {
            val thisWordSize = rng.nextInt(minWordSize, maxWordSize + 1)
            for(c in 0 until thisWordSize) {
                val randomLetterIndex = rng.nextInt(0, lessonAlphabet.length)
                lessonText += lessonAlphabet.substring(randomLetterIndex, randomLetterIndex + 1)
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
