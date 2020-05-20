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
            var editDistance = levenshteinDistance(correctText, userInputText)

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
            putExtra(TRAINING_LESSON_INDEX, intent.getIntExtra(TRAINING_LESSON_INDEX, 0))
        }
        startActivity(intent);
    }

    //TODO Would like to write tests for this
    private fun levenshteinDistance(a : String, b : String) : Int {
        if(a.isEmpty()) return b.length
        if(b.isEmpty()) return a.length

        var costMatrix = Array<Array<Int>>(a.length) {Array<Int>(b.length) {0} }
        var getCost = { i : Int, j : Int -> if(i < 0 && j < 0) 0 else if(i < 0) j + 1 else if(j < 0) i + 1 else costMatrix[i][j] }

        for(i in a.indices) {
            for (j in b.indices) {
                var substitutionCost = getCost(i -1, j - 1) + if(a[i] == b[j]) 0 else 1
                var insertionCost = getCost(i, j - 1) + 1
                var deletionCost = getCost(i - 1, j) + 1
                costMatrix[i][j] = minOf(substitutionCost, insertionCost, deletionCost)
            }
        }

        return costMatrix[a.length - 1][b.length - 1]
    }

}
