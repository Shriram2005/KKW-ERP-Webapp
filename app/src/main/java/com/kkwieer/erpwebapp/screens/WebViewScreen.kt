package com.kkwieer.erpwebapp.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

private const val DESKTOP_USER_AGENT: String =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    title: String,
    url: String,
    onNavigateBack: () -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isDesktopMode by remember { mutableStateOf(false) }
    var originalUserAgent by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    isDesktopMode = !isDesktopMode
                    webView?.let { wv ->
                        val settings = wv.settings
                        if (isDesktopMode) {
                            if (originalUserAgent == null) originalUserAgent =
                                settings.userAgentString
                            settings.userAgentString = DESKTOP_USER_AGENT
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            // Keep 100% scale; we'll adjust fit after load
                        } else {
                            settings.userAgentString = originalUserAgent
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                        }
                        wv.reload()
                    }
                },
                containerColor = if (isDesktopMode) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            ) {
                Text(
                    text = if (isDesktopMode) "📱" else "💻",
                    fontSize = 18.sp
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(
                                view: WebView,
                                url: String,
                                favicon: android.graphics.Bitmap?
                            ) {
                                isLoading = true
                                super.onPageStarted(view, url, favicon)
                            }
                            override fun onPageFinished(view: WebView, url: String) {
                                isLoading = false
                                if (isDesktopMode) {
                                    // Inject desktop-like viewport to trigger desktop layouts
                                    view.evaluateJavascript(
                                        """
                                        (function(){
                                          var m = document.querySelector('meta[name=viewport]');
                                          if(!m){ m = document.createElement('meta'); m.name='viewport'; document.head.appendChild(m); }
                                          m.setAttribute('content','width=1280');
                                        })();
                                        """.trimIndent(),
                                        null
                                    )
                                    view.postDelayed({
                                        // Fit content to screen width after viewport change
                                        repeat(4) { view.zoomOut() }
                                    }, 100)
                                }
                            }
                        }

                        // Configure settings for better rendering
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            layoutAlgorithm =
                                android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING

                            // Additional settings for better rendering
                            javaScriptCanOpenWindowsAutomatically = true
                            mediaPlaybackRequiresUserGesture = false
                            allowFileAccess = true
                            allowContentAccess = true

                            // Enable mixed content for compatibility
                            mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }

                        loadUrl(url)
                        webView = this
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(28.dp))
                }
            }
        }
    }

    BackHandler {
        val wv = webView
        if (wv != null && wv.canGoBack()) {
            wv.goBack()
        } else {
            onNavigateBack()
        }
    }
}
