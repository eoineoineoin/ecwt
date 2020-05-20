package es.eoinrul.ecwt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.eoinrul.ecwt.R

const val TRAINING_LESSON_INDEX = "es.eoinrul.ecwt.TRAINING_LESSON_INDEX"

class LevelSelectActivity : AppCompatActivity(),
    TrainingLevelFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        mLevelSelectLayoutManager = LinearLayoutManager(this)

        mLevelSelectView = findViewById<RecyclerView>(R.id.levelSelection_view).apply {
            setHasFixedSize(true)
            layoutManager = mLevelSelectLayoutManager
        }
    }

    override fun onListFragmentInteraction(lesson: KochLessonDefinitions.KochLesson?) {
        if(lesson != null) {
            // Remember the last lesson that was run // TODO This should be in the training
            var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val lastLessonPrefKey = getString(R.string.setting_last_lesson_key)
            sharedPreferences.edit().putInt(lastLessonPrefKey, lesson.lessonIndex).apply()
        }

        val intent = Intent(this, TrainingActivity::class.java).apply {
            putExtra(TRAINING_LESSON_INDEX, lesson?.lessonIndex)
        }
        startActivity(intent);
    }

    private lateinit var mLevelSelectView : RecyclerView
    private lateinit var mLevelSelectLayoutManager : RecyclerView.LayoutManager
}
