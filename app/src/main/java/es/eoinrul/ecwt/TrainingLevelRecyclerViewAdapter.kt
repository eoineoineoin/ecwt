package es.eoinrul.ecwt

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import es.eoinrul.ecwt.R


import es.eoinrul.ecwt.TrainingLevelFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_training_level.view.*

class TrainingLevelRecyclerViewAdapter(
    private val mLessons: List<KochLessonDefinitions.KochLesson>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<TrainingLevelRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as KochLessonDefinitions.KochLesson
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_training_level, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = mLessons[position]
        holder.mIdView.text = "Lesson " + lesson.indexForHumans().toString()
        holder.mContentView.text = lesson.toString()

        with(holder.mView) {
            tag = lesson
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mLessons.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
