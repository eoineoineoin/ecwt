package es.eoinrul.ecwt

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

//const val PREFERENCES_FILE = "es.eoinrul.ecwt.PREFERENCES"

class SettingsActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cw_settings)

        // Sounds: WPM; Farnsworth; (character speed versus word speed) Tone freq
        // Koch: Lesson duration; word size
        //var preferences = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
        mLastSenderSettings = DitDahGeneratorSettings(this)
    }

    override fun onResume() {
        super.onResume()
        var sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    // Unhook our listener, and shut down any sound sample stream we might have made
    override fun onPause() {
        var sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
        mSampleOutputStream?.quit()
        mSampleOutputStream = null
        super.onPause()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        var newSettings = DitDahGeneratorSettings(this)

        // Play a sample sound to demo new settings if the relevant ones have changed
        if(newSettings.toneFrequency != mLastSenderSettings.toneFrequency
            || newSettings.wordsPerMinute != mLastSenderSettings.wordsPerMinute
            || newSettings.farnsworthWordsPerMinute != mLastSenderSettings.farnsworthWordsPerMinute) {
            mSampleOutputStream?.quit()
            var sampleOutputStream = DitDahSoundStream(newSettings)
            sampleOutputStream.enqueue(StringToSoundSequence("CQ"))
            mSampleOutputStream = sampleOutputStream
            mLastSenderSettings = newSettings
        }
    }

    var mLastSenderSettings = DitDahGeneratorSettings()
    var mSampleOutputStream : DitDahSoundStream? = null
}
