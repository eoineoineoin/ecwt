package es.eoinrul.ecwt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.eoinrul.ecwt.R

const val TRAINING_ALPHABET = "es.eoinrul.ecwt.TRAINING_ALPHABET"

class LevelSelectActivity : AppCompatActivity(),
    TrainingLevelFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        mLevelSelectLayoutManager = LinearLayoutManager(this)

        mLevelSelectView = findViewById<RecyclerView>(R.id.levelSelection_view).apply {
            setHasFixedSize(true)
            layoutManager = mLevelSelectLayoutManager
            //adapter = mLevelSelectViewAdapter
        }
    }

    override fun onListFragmentInteraction(lesson: KochLessonDefinitions.KochLesson?) {
        val intent = Intent(this, TrainingActivity::class.java).apply {
            putExtra(TRAINING_ALPHABET, lesson?.getAlphabet())
        }
        startActivity(intent);
    }


    private lateinit var mLevelSelectView : RecyclerView
    private lateinit var mLevelSelectLayoutManager : RecyclerView.LayoutManager
}
