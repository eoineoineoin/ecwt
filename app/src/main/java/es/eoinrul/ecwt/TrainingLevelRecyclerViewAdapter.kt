package es.eoinrul.ecwt

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.preference.PreferenceManager
import es.eoinrul.ecwt.R


import es.eoinrul.ecwt.TrainingLevelFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_training_level.view.*

class TrainingLevelRecyclerViewAdapter(
    private val mLessons: List<KochLessonDefinitions.KochLesson>,
    private val mListener: OnListFragmentInteractionListener?,
    private val mLastLessonIndex : Int
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
        // If we have remembered the last lesson, there's a "fake" item at position 0
        // to allow fast continuing. Then we need to offset the position, to account for it
        var positionToIndexOffset = if(mLastLessonIndex > 0) 1 else 0
        val lessonIndex = if(position == 0 && mLastLessonIndex > 0) {
            mLastLessonIndex
        } else position - positionToIndexOffset

        val lesson = mLessons[lessonIndex]
        holder.mIdView.text = lesson.indexForHumans().toString()
        holder.mContentView.text = lesson.toString()
        holder.mSymbolView.text = lesson.newSignsAsString()

        with(holder.mView) {
            tag = lesson
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        if (mLastLessonIndex > 0) {
            return mLessons.size + 1 // +1 for the "last lesson" button
        }
        return mLessons.size // No point showing the "last lesson" if we never did one before
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val mSymbolView : TextView = mView.symbols

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
