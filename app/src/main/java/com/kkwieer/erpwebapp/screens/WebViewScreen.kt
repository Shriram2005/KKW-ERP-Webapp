package com.kkwieer.erpwebapp.screens

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

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
    var isRefreshing by remember { mutableStateOf(false) }
    var currentUrl by remember { mutableStateOf(url) }
    var isInitialLoad by remember { mutableStateOf(true) }

    // Function to refresh the webview
    fun refreshWebView() {
        isRefreshing = true
        webView?.let { wv ->
            wv.reload()
        }
    }

    // Function to apply desktop/mobile mode
    fun applyUserAgentMode(webViewInstance: WebView) {
        val settings = webViewInstance.settings
        if (isDesktopMode) {
            if (originalUserAgent == null) originalUserAgent = settings.userAgentString
            settings.userAgentString = DESKTOP_USER_AGENT
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
        } else {
            settings.userAgentString = originalUserAgent
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
        }
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    // Keep swipeRefreshState.isRefreshing always in sync with isRefreshing (one way)
    LaunchedEffect(isRefreshing) {
        if (swipeRefreshState.isRefreshing != isRefreshing) {
            swipeRefreshState.isRefreshing = isRefreshing
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                // Desktop/Mobile toggle button (top)
                SmallFloatingActionButton(
                    onClick = {
                        isDesktopMode = !isDesktopMode
                        webView?.let { wv ->
                            applyUserAgentMode(wv)
                            refreshWebView() // Refresh to apply the new user agent
                        }
                    },
                    containerColor = if (isDesktopMode) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    contentColor = if (isDesktopMode) {
                        MaterialTheme.colorScheme.onTertiary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                ) {
                    Text(
                        text = if (isDesktopMode) "📱" else "💻",
                        fontSize = 16.sp
                    )
                }

                // Refresh button (bottom)
                SmallFloatingActionButton(
                    onClick = { refreshWebView() },
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        if (event.key == Key.F5 || (event.key == Key.R && event.isCtrlPressed)) {
                            refreshWebView()
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { refreshWebView() }
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
                                    if (isInitialLoad) {
                                        isLoading = true
                                    }
                                    currentUrl = url
                                    super.onPageStarted(view, url, favicon)
                                }

                                override fun onPageFinished(view: WebView, url: String) {
                                    isLoading = false
                                    isRefreshing = false
                                    isInitialLoad = false
                                    currentUrl = url

                                    if (isDesktopMode) {
                                        // Inject desktop-like viewport to trigger desktop layouts
                                        view.evaluateJavascript(
                                            """
                                            (function(){
                                              var m = document.querySelector('meta[name=viewport]');
                                              if(!m){ 
                                                m = document.createElement('meta'); 
                                                m.name='viewport'; 
                                                document.head.appendChild(m); 
                                              }
                                              m.setAttribute('content','width=1280');
                                              
                                              // Force a re-layout
                                              document.body.style.minWidth = '1280px';
                                            })();
                                            """.trimIndent(),
                                            null
                                        )

                                        // Adjust zoom after a short delay
                                        view.postDelayed({
                                            try {
                                                repeat(3) { view.zoomOut() }
                                            } catch (e: Exception) {
                                                // Handle any zoom exceptions gracefully
                                            }
                                        }, 300)
                                    }
                                    super.onPageFinished(view, url)
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    errorCode: Int,
                                    description: String?,
                                    failingUrl: String?
                                ) {
                                    isLoading = false
                                    isRefreshing = false
                                    isInitialLoad = false
                                    super.onReceivedError(view, errorCode, description, failingUrl)
                                }
                            }

                            // Configure settings for better rendering and refresh support
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

                                // Additional settings for better rendering and refresh
                                javaScriptCanOpenWindowsAutomatically = true
                                mediaPlaybackRequiresUserGesture = false
                                allowFileAccess = true
                                allowContentAccess = true
                                cacheMode = android.webkit.WebSettings.LOAD_DEFAULT

                                // Enable mixed content for compatibility
                                mixedContentMode =
                                    android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                            }

                            // Apply initial user agent mode
                            applyUserAgentMode(this)
                            loadUrl(url)
                            webView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Center loading indicator (only for initial load)
            if (isLoading && isInitialLoad) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    // Enhanced back navigation with WebView history support
    BackHandler {
        val wv = webView
        if (wv != null && wv.canGoBack()) {
            wv.goBack()
        } else {
            onNavigateBack()
        }
    }

    // Auto-hide refresh indicator after a reasonable time if stuck
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(10000) // 10 seconds timeout
            if (isRefreshing) {
                isRefreshing = false
            }
        }
    }
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}
