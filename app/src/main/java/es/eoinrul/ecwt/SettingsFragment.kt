package es.eoinrul.ecwt

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import es.eoinrul.ecwt.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
