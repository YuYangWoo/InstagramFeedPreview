package com.example.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.login.databinding.FragmentLoginBinding
import com.example.model.Login
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.URLDecoder

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLogin()
        initObserver()
    }

    private fun setLogin() {
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
                                val accessToken = decodedUrl.split("code=").getOrNull(1)?.split("#_")?.getOrNull(0) ?: ""
                                val login = Login(
                                    "520355146868539",
                                    "cd3590d3a75b81c5156a67034b1d6280",
                                    "authorization_code",
                                    "https://yang-droid.tistory.com/",
                                    accessToken
                                )
                                Log.d(TAG, "accessToken is $accessToken")
                                loginViewModel.requestAccessToken(login)
                                return true
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
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.uiState.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            val request = NavDeepLinkRequest.Builder
                                .fromUri("app://example.app/boardFragment/${state.data.accessToken}".toUri())
                                .build()
                            findNavController().navigate(request)
                            Log.d(TAG, state.data.toString())
                        }
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            Log.d(TAG, state.message)
                        }
                        is UiState.Empty -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}
