package dominando.android.webview

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = webView.settings
        settings.javaScriptEnabled = true

        webView.addJavascriptInterface(this, "dominando")
        webView.loadUrl("file:///android_asset/app_page.html")

//        webView.loadUrl("http://www.nglauber.com.br")

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                view.loadUrl(request.url.toString())
                return false
            }

            /*
            quando a página começa a carregar
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            quando a página termina de carregar
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }*/
        }
    }

    @JavascriptInterface
    fun showToast(s: String, t: String) {
        Toast.makeText(this, "Nome: $s Idade: $t", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
