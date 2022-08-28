package es.eoinrul.ecwt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible

class TrainingResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_results)

        val userInputText = intent.getStringExtra(TRAINING_COPIED)?.toLowerCase()
        val correctText = intent.getStringExtra(TRAINING_ANSWER)?.toLowerCase()
        displayResults(userInputText, correctText)

        // Setup the "next level" button
        mNextLevelNumberText = findViewById<TextView>(R.id.trainingNextLevelNumber)
        mNextLevelContentsText = findViewById<TextView>(R.id.trainingNextLevelContents)

        val nextLevelIndex = intent.getIntExtra(TRAINING_LESSON_INDEX, 0) + 1
        val nextLesson = KochLessonDefinitions.KochLesson(nextLevelIndex)
        val nextLessonCharacters = nextLesson.newCharacters()
        if(!nextLessonCharacters.isEmpty()) {
            mNextLevelNumberText?.text = nextLesson.indexForHumans().toString()
            mNextLevelContentsText?.text = nextLesson.newCharacters() + "  " + nextLesson.newSignsAsString()
        } else {
            // Next lesson didn't add any new characters, so we must be on the last lesson.
            // Hide the "next" button, since there's nothing it could reasonably do
            val nextLessonContainer = findViewById<View>(R.id.trainingNextLevelContainer)
            nextLessonContainer.visibility = View.GONE
        }
    }

    fun onTryAgainButtonPressed(view : View) {
        val intent = Intent(this, TrainingActivity::class.java).apply {
            putExtra(TRAINING_LESSON_INDEX, intent.getIntExtra(TRAINING_LESSON_INDEX, 0))
        }
        startActivity(intent)
    }

    fun onAddCharacterButtonPressed(view : View) {
        val intent = Intent(this, TrainingActivity::class.java).apply {
            putExtra(TRAINING_LESSON_INDEX, intent.getIntExtra(TRAINING_LESSON_INDEX, 0) + 1)
        }
        startActivity(intent)
    }

    private fun displayResults(userInputText : String?, correctText : String?) {
        if(userInputText == null || correctText == null) {
            findViewById<TextView>(R.id.resultSummary).text =
                getString(R.string.training_results_error)
            return
        }

        val comparisonResult = levenshteinDistance(userInputText, correctText)
        val editDistance = comparisonResult.mEditDistance

        val fractionCorrect = (correctText.length - editDistance).toFloat() / correctText.length.toFloat()
        val percentCorrect = if (editDistance == 0) {
            100
        } else {
            maxOf(0, (fractionCorrect * 100.0f).toInt())
        }

        findViewById<TextView>(R.id.resultSummary).text = getString(R.string.training_results_summary, percentCorrect, editDistance)

        // Add extra feedback if the user didn't add any spaces
        var userAddedSpaces = userInputText.indexOf(' ') != -1
        findViewById<LinearLayout>(R.id.spacesHelpContainer).isVisible = !userAddedSpaces

        var detailContainer = findViewById<TextView>(R.id.resultDetails)
        detailContainer.text = HtmlCompat.fromHtml(formatEditDetails(comparisonResult.mEdits), 0)
    }

    private fun formatColorId(c : Int) : String {
        return "<font color=\"#" + Integer.toHexString(getColor(c)).substring(2) + "\">"
    }

    private fun formatEditDetails(edits : List<SingleEdit>) : String {
        var formattedEditDetail = ""
        for(edit in edits) {
            formattedEditDetail += when (edit.mEditType) {
                EditType.NO_CHANGE -> {
                    formatColorId(R.color.colorResultDetailCorrect) + edit.mCharInfo + "</font>"
                }
                EditType.SUBSTITUTE -> {
                    formatColorId(R.color.colorResultDetailIncorrect) +
                        "<strike>" + edit.mCharInfo + "</strike>" +
                        edit.mReplacementChar + "</font>"
                }
                EditType.DELETE -> {
                    formatColorId(R.color.colorResultDetailIncorrect) +
                    "<strike>" + edit.mCharInfo + "</strike>" + "</font>"
                }
                else -> { // INSERT
                    formatColorId(R.color.colorResultDetailIncorrect) + edit.mCharInfo + "</font>"
                }
            }
        }

        return formattedEditDetail
    }

    enum class EditType {
        NO_CHANGE,
        SUBSTITUTE,
        INSERT,
        DELETE,
    }

    class SingleEdit {
        val mEditType: EditType

        // The correct character, or the one to modify/insert/delete
        val mCharInfo: String

        // With SUBSTITUTE, the character that is inserted
        val mReplacementChar: String

        constructor(editType : EditType, char1 : Char, char2 : Char = Char.MIN_VALUE)
        {
            mEditType = editType
            mCharInfo = if (char1 == ' ') " · " else char1.toString()
            mReplacementChar = if (char2 == ' ') " · " else char2.toString()
        }
    }

    class ComparisonResult (
        val mEditDistance : Int,
        val mEdits : List<SingleEdit>
    )

    private fun strToEdits(type : EditType, s : String) : List<SingleEdit>
    {
        var ret : MutableList<SingleEdit> = mutableListOf<SingleEdit>()
        for(c in s) {
            ret.add(SingleEdit(type, c))
        }
        return ret
    }

    //TODO Would like to write tests for this
    private fun levenshteinDistance(a : String, b : String) : ComparisonResult {
        if(a.isEmpty()) return ComparisonResult(b.length, strToEdits(EditType.INSERT, b))
        if(b.isEmpty()) return ComparisonResult(a.length, strToEdits(EditType.DELETE, a))

        var costMatrix = Array<Array<Int>>(a.length) {Array<Int>(b.length) {0} }
        val getCost = { i : Int, j : Int -> if(i < 0 && j < 0) 0 else if(i < 0) j + 1 else if(j < 0) i + 1 else costMatrix[i][j] }

        for(i in a.indices) {
            for (j in b.indices) {
                val substitutionCost = getCost(i - 1, j - 1) + if (a[i] == b[j]) 0 else 1
                val insertionCost = getCost(i, j - 1) + 1
                val deletionCost = getCost(i - 1, j) + 1
                costMatrix[i][j] = minOf(substitutionCost, insertionCost, deletionCost)
            }
        }

        // Now, walk the cost matrix backwards to build the minimal edits
        var curI : Int = a.length - 1
        var curJ : Int = b.length - 1
        val edits : MutableList<SingleEdit> = arrayListOf()

        while(curI >= 0 || curJ >= 0) {
            val curCost = getCost(curI, curJ)
            val substitutionCost = getCost(curI - 1, curJ - 1)
            val insertionCost = getCost(curI, curJ - 1)
            val deletionCost = getCost(curI - 1, curJ)
            val minCost = minOf(substitutionCost, insertionCost, deletionCost)
            if(minCost == substitutionCost && curCost == substitutionCost && curJ >= 0) {
                // Equal
                edits.add(SingleEdit(EditType.NO_CHANGE, b[curJ]))
                curI -= 1
                curJ -= 1
            }
            else if(minCost == substitutionCost && curI >= 0 && curJ >= 0) {
                // Substitute
                edits.add(SingleEdit(EditType.SUBSTITUTE, a[curI], b[curJ]))
                curI -= 1
                curJ -= 1
            }
            else if(minCost == insertionCost && curJ >= 0) {
                // Insert
                edits.add(SingleEdit(EditType.INSERT, b[curJ]))
                curJ -= 1
            }
            else {
                // Delete
                edits.add(SingleEdit(EditType.DELETE, a[curI]))
                curI -= 1
            }
        }

        return ComparisonResult(costMatrix[a.length - 1][b.length - 1],
            edits.reversed())
        }

    private var mNextLevelNumberText : TextView? = null
    private var mNextLevelContentsText : TextView? = null
}
