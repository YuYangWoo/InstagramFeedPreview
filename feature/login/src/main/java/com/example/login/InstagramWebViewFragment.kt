package com.example.login

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.library.binding.BindingFragment
import com.example.login.databinding.FragmentInstagramBinding
import com.example.model.Login
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.net.URLDecoder

@AndroidEntryPoint
class InstagramWebViewFragment : BindingFragment<FragmentInstagramBinding>(R.layout.fragment_instagram) {
    private val instagramViewModel by activityViewModels<InstagramViewModel>()
    override fun init() {
        super.init()

        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.url?.let { url ->
                        val decodedUrl = URLDecoder.decode(url, "utf-8")
                        if (decodedUrl.contains("code=")) {
                            try {
                                val accessToken = decodedUrl.split("code=")[1].split("#_")[0]
                                val loginDTO = Login(
                                    "520355146868539",
                                    "cd3590d3a75b81c5156a67034b1d6280",
                                    "authorization_code",
                                    "https://yang-droid.tistory.com/",
                                    accessToken
                                )
                                Log.d(TAG, "accessToken is $accessToken")
                                instagramViewModel.requestAccessToken(loginDTO)
                                findNavController().navigate(InstagramWebViewFragmentDirections.actionInstagramFragmentToFeatureBoardNavigation())
                            } catch (e: Exception) {
                                Log.d(TAG, e.message.toString())
                            }
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            settings.javaScriptEnabled = true
            loadUrl("https://api.instagram.com/oauth/authorize?client_id=520355146868539&redirect_uri=https://yang-droid.tistory.com/&scope=user_profile,user_media&response_type=code")
        }
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            instagramViewModel.token.collectLatest { tokenDTO ->
                instagramViewModel.saveUserAccessToken(tokenDTO.accessToken)
//                instagramViewModel.requestBoardItem(tokenDTO.accessToken)
            }
        }
    }

    companion object {
        private const val TAG = "InstagramFragment"
    }

}
