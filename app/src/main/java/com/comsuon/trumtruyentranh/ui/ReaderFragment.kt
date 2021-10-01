package com.comsuon.trumtruyentranh.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.model.Chapter
import com.comsuon.trumtruyentranh.repo.model.MangaSite
import com.comsuon.trumtruyentranh.repo.network.ScraperConfigs
import com.comsuon.trumtruyentranh.ui.adapters.ImageAdapter
import com.comsuon.trumtruyentranh.ui.utils.Constants
import com.comsuon.trumtruyentranh.ui.utils.getScraperConfigsFromUrl
import com.comsuon.trumtruyentranh.ui.viewmodels.MangaViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_reader.*
import org.jsoup.Jsoup


class ReaderFragment : Fragment() {
    companion object {
        const val CHAPTER_URL_KEY = "CHAPTER_URL_KEY"
    }

    private val vm: MangaViewModel by activityViewModels()

    private var scraperConfigs: ScraperConfigs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_reader, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_image.layoutManager = LinearLayoutManager(context)

        vm.currentChapter.observe(viewLifecycleOwner, Observer { chapter ->
            requireActivity().title = chapter.title
            if (chapter.url.contains(MangaSite.TRUYEN_TRANH_TUAN.host).not()) {
                vm.getImageList(chapter, scraperConfigs)
            } else {
                getHtmlPage(chapter = chapter)
            }
        })

        vm.nextChapter.observe(viewLifecycleOwner, Observer { nextChapter ->
            requireActivity().invalidateOptionsMenu()
        })

        vm.imageList.observe(viewLifecycleOwner, Observer { imageList ->
            setImageList(imageList)
        })

        arguments?.getParcelable<Chapter>(CHAPTER_URL_KEY)?.let { chapter ->
            scraperConfigs = requireContext().getScraperConfigsFromUrl(chapter.url)
            readChapter(chapter)
        }

    }

    private fun getHtmlPage(chapter: Chapter) {
        try {
            val browser = WebView(requireContext())
            browser.visibility = View.INVISIBLE
            browser.setLayerType(View.LAYER_TYPE_NONE, null)
            browser.settings.javaScriptEnabled = true
            browser.settings.blockNetworkImage = true
            browser.settings.domStorageEnabled = false
            browser.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            browser.settings.loadsImagesAutomatically = false
            browser.settings.setGeolocationEnabled(false)
            browser.settings.setSupportZoom(false)
            browser.addJavascriptInterface(JSHtmlInterface(), "JSBridge")
            browser.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    browser.loadUrl("javascript:window.JSBridge.showImageList('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                }
            }

            browser.loadUrl(chapter.url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_reader, menu)
        val nextChapter = menu.findItem(R.id.btn_next_chapter)
        nextChapter.isVisible = vm.nextChapter.value != null
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_next_chapter -> {
                vm.nextChapter.value?.let {
                    rv_image.adapter = null
                    readChapter(chapter = it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun readChapter(chapter: Chapter) {
        vm.setReadChapter(chapter)
        val sharedPreferences =
            requireContext().getSharedPreferences(Constants.PREF_KEY, MODE_PRIVATE)
        sharedPreferences.edit {
            vm.mangaDetails.value?.result?.url?.let { url ->
                putString(url, Gson().toJson(chapter))
            }
        }
    }

    private inner class JSHtmlInterface {
        @android.webkit.JavascriptInterface
        fun showImageList(html: String) {
            requireActivity().runOnUiThread {
                val doc = Jsoup.parse(html)
                val imageList = scraperConfigs?.let {
                    doc.select(it.imagesTag).map { imgTag ->
                        imgTag.attr("src")
                    }
                }

                imageList?.let {
                    setImageList(it)
                }
            }
        }
    }

    private fun setImageList(imageList: List<String>) {
        val adapter = ImageAdapter(imageList)
        rv_image.adapter = adapter
    }
}