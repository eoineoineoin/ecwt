package es.eoinrul.ecwt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import kotlin.random.Random

class TrainingActivity : AppCompatActivity(),
    DitDahSoundStream.StreamNotificationListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traning)

        initSoundPlayer()

        val alphabet = intent.getStringExtra(TRAINING_ALPHABET);
        startLesson(alphabet!!) //TODO Only after user input is ready
    }

    private fun initSoundPlayer() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val generatorSettings = DitDahGeneratorSettings()
        generatorSettings.initFromPreferences(sharedPreferences)

        mSoundPlayer = DitDahSoundStream(generatorSettings)
        mSoundPlayer.streamNotificationListener = this
    }

    private fun startLesson(alphabet : String) {
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
                val randomLetterIndex = rng.nextInt(0, alphabet.length)
                lessonText += alphabet.substring(randomLetterIndex, randomLetterIndex + 1)
            }
            lessonText += " "
        }

        mSoundPlayer.enqueue(StringToSoundSequence(lessonText))

    }

    private lateinit var mSoundPlayer : DitDahSoundStream
    override fun streamFinished(stream: DitDahSoundStream) {
    }

}
