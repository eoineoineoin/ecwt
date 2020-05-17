package es.eoinrul.ecwt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_level_select.*
import kotlin.math.min

class TrainingResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_results)

        var userInputText = intent.getStringExtra(TRAINING_COPIED)?.toLowerCase()
        var correctText = intent.getStringExtra(TRAINING_ANSWER)?.toLowerCase()

        if(userInputText != null && correctText != null) {
            var editDistance = levenshtein(correctText, userInputText)

            var fractionCorrect = (correctText.length - editDistance).toFloat() / correctText.length.toFloat()
            var percentCorrect = if (editDistance == 0) {
                100
            } else {
                (fractionCorrect * 100.0f).toInt()
            }

            findViewById<TextView>(R.id.resultSummary).text = getString(R.string.training_results_summary, percentCorrect, editDistance)
        }
        else {
            findViewById<TextView>(R.id.resultSummary).text =
                getString(R.string.training_results_error)
        }

        // TODO Could have a per-character breakdown here
    }

    fun onTryAgainButtonPressed(view : View) {
        val intent = Intent(this, TrainingActivity::class.java).apply {
            putExtra(TRAINING_ALPHABET, intent.getStringExtra(TRAINING_ALPHABET))
        }
        startActivity(intent);
    }

    private fun levenshtein(a : String, b : String) : Int {
        if(a.isEmpty()) return b.length
        if(b.isEmpty()) return a.length

        if(a[0] == b[0]) {
            return levenshtein(a.substring(1), b.substring(1))
        } else {
            var insertDistance = levenshtein(a, b.substring(1))
            var deleteDistance = levenshtein(a.substring(1), b)
            var substituteDistance = levenshtein(a.substring(1), b.substring(1))
            return 1 + min(min(insertDistance, deleteDistance), substituteDistance)
        }
    }
}
