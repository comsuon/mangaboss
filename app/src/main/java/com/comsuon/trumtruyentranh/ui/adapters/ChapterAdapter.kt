package com.comsuon.trumtruyentranh.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.ui.adapters.ChapterAdapter.ChapterViewHolder

class ChapterAdapter(
    private val chapterList: List<Chapter>,
    val chapterClickListener: ChapterClickListener
) :
    RecyclerView.Adapter<ChapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_chapter_row, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.onBind(chapterList[position])
    }

    override fun getItemCount(): Int = chapterList.size

    inner class ChapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val chapterRow: TextView = view.findViewById(R.id.tv_chapter_name)
        fun onBind(chapter: Chapter) {
            chapterRow.text = chapter.title
            chapterRow.setOnClickListener {
                chapterClickListener.onChapterClick(chapter)
            }
        }
    }
}

interface ChapterClickListener {
    fun onChapterClick(chapter: Chapter)
}