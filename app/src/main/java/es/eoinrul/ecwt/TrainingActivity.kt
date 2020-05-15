package es.eoinrul.ecwt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import kotlin.random.Random

class TrainingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traning)

        initSoundPlayer()

        val alphabet = intent.getStringExtra(TRAINING_ALPHABET);
        startLesson(alphabet!!) //TODO Only after user input is ready
    }

    private fun initSoundPlayer() {
        val generatorSettings = DitDahGeneratorSettings()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        generatorSettings.toneFrequency = sharedPreferences.getInt("sender_tone", generatorSettings.toneFrequency)

        mSoundPlayer = DitDahSoundStream(generatorSettings)
    }

    private fun startLesson(alphabet : String) {
        val generatorSettings = DitDahGeneratorSettings()
        val lessonLengthInMinutes = 1 // TODO These should be configurable from the settings window
        val wordSize = 5
        val numberOfWords = generatorSettings.farnsworthWordsPerMinute * lessonLengthInMinutes

        var lessonText = String()
        var rng = Random.Default
        for(i in 0 until numberOfWords) {
            for(c in 0 until wordSize) {
                val randomLetterIndex = rng.nextInt(0, alphabet.length)
                lessonText += alphabet.substring(randomLetterIndex, randomLetterIndex + 1)
            }
            lessonText += " "
        }

        mSoundPlayer.enqueue(StringToSoundSequence(lessonText))

    }

    private lateinit var mSoundPlayer : DitDahSoundStream

}
