package com.example.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.login.databinding.FragmentLoginBinding
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.Login
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.URLDecoder

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val loginViewModel: LoginViewModel by viewModels()

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
        initLogin()
        initCollect()
    }

    private fun initLogin() {
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
                                val uiLogin = UiLogin(
                                    BuildConfig.CLIENT_ID,
                                    BuildConfig.CLIENT_SECRET,
                                    "authorization_code",
                                    "https://yang-droid.tistory.com/",
                                    accessToken
                                )
                                Log.d(TAG, "accessToken is $accessToken")
                                loginViewModel.event(Contract.Event.OnUpdateLoginInfo(uiLogin))
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
            loadUrl("https://api.instagram.com/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}&redirect_uri=https://yang-droid.tistory.com/&scope=user_profile,user_media&response_type=code")
        }
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.loginUiState.collectLatest { loginUiState ->
                    when (loginUiState) {
                        is LoginUiState.Error -> {
                            binding.progressBar.isVisible = false

                            when (loginUiState.errorState) {
                                is LoginUiState.Error.ErrorState.NetworkError -> {
                                    handleError(loginUiState.errorState.message)
                                }
                                is LoginUiState.Error.ErrorState.DefaultError -> {
                                    handleError(loginUiState.errorState.message)
                                }
                            }
                        }
                        LoginUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is LoginUiState.Success -> {
                            binding.progressBar.isVisible = false

                            if (loginUiState.loginState.isShowBoardFragment) {
                                val request =
                                    NavDeepLinkRequest.Builder.fromUri("app://example.app/boardFragment/${loginUiState.loginState.accessToken}".toUri())
                                        .build()
                                findNavController().navigate(request)
                            }

                        }

                        LoginUiState.Idle -> {
                            // Nothing
                        }
                    }
                }
            }
        }
    }

    private fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        Log.d(TAG, message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "LoginFragment"
    }

}
