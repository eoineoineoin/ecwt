package com.example.ecwt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecwt.dummy.DummyContent

class LevelSelectAdaptor : RecyclerView.Adapter<LevelSelectAdaptor.LevelSelectViewHolder>() {
    class LevelSelectViewHolder(val textView : LinearLayout) : RecyclerView.ViewHolder(textView)

    override fun getItemCount(): Int {
        return 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelSelectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_training_level, parent, false) as LinearLayout

        return LevelSelectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LevelSelectViewHolder, position: Int) {
        holder.textView.findViewById<TextView>(R.id.content).text = "OMG"
    }

}

class LevelSelectActivity : AppCompatActivity(),
    TrainingLevelFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)

        mLevelSelectLayoutManager = LinearLayoutManager(this)
        mLevelSelectViewAdapter = LevelSelectAdaptor()

        mLevelSelectView = findViewById<RecyclerView>(R.id.levelSelection_view).apply {
            setHasFixedSize(true)
            layoutManager = mLevelSelectLayoutManager
            adapter = mLevelSelectViewAdapter
        }
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        TODO("Not yet implemented")
    }


    private lateinit var mLevelSelectView : RecyclerView
    private lateinit var mLevelSelectLayoutManager : RecyclerView.LayoutManager
    private lateinit var mLevelSelectViewAdapter : LevelSelectAdaptor
}
