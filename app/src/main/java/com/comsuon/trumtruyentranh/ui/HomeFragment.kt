package com.comsuon.trumtruyentranh.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.comsuon.trumtruyentranh.R

class HomeFragment : Fragment() {

    companion object {
        const val MANGA_URL_KEY = "MANGA_URL_KEY"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context, R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn_get_manga = view.findViewById<Button>(R.id.btn_get_manga)
        val et_manga_url = view.findViewById<EditText>(R.id.et_manga_url_input)

        btn_get_manga.setOnClickListener {

            val url = et_manga_url.text?.toString() ?: ""
            if (url.isBlank().not()) {
                val arg = Bundle().apply {
                    putString(MANGA_URL_KEY, url)
                }
                findNavController().navigate(
                    R.id.action_fragmentHome_to_detailsFragment,
                    arg
                )
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.home_toast_input_url),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}