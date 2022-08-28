package com.comsuon.trumtruyentranh.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.comsuon.trumtruyentranh.R
import com.comsuon.trumtruyentranh.repo.network.ScraperConfigs
import com.comsuon.trumtruyentranh.ui.utils.getScraperConfigPathFromUrl
import com.comsuon.trumtruyentranh.ui.utils.getScraperConfigsFromUrl

class MangaBrowserFragment : Fragment() {
    lateinit var mBrowser: WebView
    lateinit var mRoot: View
    private var mUrl: String? = null
    private var mScraperConfigs: ScraperConfigs? = null

    companion object {
        const val WEB_URL = "WEB_URL"
        fun newInstance(url: String): MangaBrowserFragment {
            val bundle = bundleOf(Pair(WEB_URL, url))
            return MangaBrowserFragment().apply {
                arguments = bundle
            }
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mRoot = inflater.inflate(R.layout.fragment_browser, null, false)
        return mRoot
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBrowser = mRoot.findViewById(R.id.webview)
        mBrowser.settings.userAgentString = "Chrome"
        mBrowser.settings.javaScriptEnabled = true
        mBrowser.settings.domStorageEnabled = true
        val webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (isMangaDetailLink(request?.url.toString())) {
                    findNavController().navigate(
                        R.id.action_mangaBrowserFragment_to_fragmentDetails,
                        Bundle().apply {
                            putString(HomeFragment.MANGA_URL_KEY, request?.url.toString())
                        })
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        mBrowser.webViewClient = webClient

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mBrowser.canGoBack()) {
                        mBrowser.goBack()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
            )
    }

    override fun onResume() {
        super.onResume()
        mUrl = arguments?.getString(WEB_URL, "")
        mScraperConfigs = context?.getScraperConfigsFromUrl(mUrl)
        if (mUrl.isNullOrEmpty().not()) {
            mBrowser.loadUrl(mUrl!!)
        }
    }

    private fun isMangaDetailLink(url: String?): Boolean {
        if (url.isNullOrEmpty()) return false

        //retry getting config from detail side url
        if (mScraperConfigs == null) {
            mScraperConfigs = context?.getScraperConfigsFromUrl(url)
        }
        if (mScraperConfigs == null) return false

        val strippedPath = url.replace("www.","").removePrefix(mScraperConfigs!!.detailBasePath)
        val parts = strippedPath.removeSuffix("/").split("/")
        return parts.size == 1
    }
}