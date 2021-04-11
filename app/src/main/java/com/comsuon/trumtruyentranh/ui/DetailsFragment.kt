package com.comsuon.trumtruyentranh.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.ui.adapters.ChapterAdapter
import com.comsuon.trumtruyentranh.ui.adapters.ChapterClickListener
import com.comsuon.trumtruyentranh.ui.utils.Constants
import com.comsuon.trumtruyentranh.ui.utils.getScraperConfigsFromUrl
import com.comsuon.trumtruyentranh.ui.viewmodels.MangaViewModel
import com.google.gson.Gson

class DetailsFragment : Fragment() {

    private val vm: MangaViewModel by activityViewModels()

    private lateinit var rootView: View

    private lateinit var imvThumbnail: ImageView

    private lateinit var tvSummary: TextView

    private lateinit var tvTitle: TextView

    private lateinit var tvCurrentChapter: TextView

    private lateinit var rvChapterList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = View.inflate(context, R.layout.fragment_details, null)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imvThumbnail = rootView.findViewById(R.id.imv_manga_thumbnail)
        rvChapterList = rootView.findViewById(R.id.rv_chapter_list)
        tvTitle = rootView.findViewById(R.id.tv_manga_title)
        tvSummary = rootView.findViewById(R.id.tv_manga_summary)
        tvCurrentChapter = rootView.findViewById(R.id.tv_current_chapter)

        bindData()
    }

    override fun onStart() {
        super.onStart()
        val mangaUrl = arguments?.getString(HomeFragment.MANGA_URL_KEY)

        if (mangaUrl.isNullOrBlank().not()) {
            val scraperConfigs = context?.getScraperConfigsFromUrl(mangaUrl)

            vm.getMangaDetails(mangaUrl = mangaUrl, scraperConfigs = scraperConfigs)
        }
    }

    private fun bindData() {
        vm.mangaDetails.observe(viewLifecycleOwner, Observer { details ->

            with(details.result) {
                requireContext().getSharedPreferences(Constants.PREF_KEY, MODE_PRIVATE).edit {
                    putString(Constants.CURRENT_MANGA_PREF_KEY, url)
                }

                tvTitle.text = title
                tvSummary.text = summary
                Glide.with(this@DetailsFragment).load(thumbnailUrl).into(imvThumbnail)
                requireActivity().title = title

                val adapter = ChapterAdapter(chapters, object : ChapterClickListener {
                    override fun onChapterClick(chapter: Chapter) {
                        findNavController().navigate(
                            R.id.action_fragmentDetails_to_fragmentReader,
                            Bundle().apply {
                                putParcelable(ReaderFragment.CHAPTER_URL_KEY, chapter)
                            })
                    }
                })
                rvChapterList.layoutManager = LinearLayoutManager(context)
                rvChapterList.adapter = adapter

                val json =
                    requireContext()
                        .getSharedPreferences(Constants.PREF_KEY, MODE_PRIVATE)
                        .getString(url, "")
                if (json.isNullOrBlank().not()) {
                    tvCurrentChapter.isVisible = true
                    val chapter = Gson().fromJson(json, Chapter::class.java)
                    tvCurrentChapter.text =
                        getString(R.string.details_current_read_chapter, chapter.title)
                    tvCurrentChapter.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_fragmentDetails_to_fragmentReader,
                            Bundle().apply {
                                putParcelable(ReaderFragment.CHAPTER_URL_KEY, chapter)
                            })
                    }
                } else {
                    tvCurrentChapter.isVisible = false
                }
            }

        })
    }

}