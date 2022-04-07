package com.simplicity.simplicityaclientforreddit.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.BaseFragment
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.custom.WebClientClass


class WebViewFragment(val url: String) : BaseFragment() {
    private lateinit var webView: WebView

    companion object {
        fun newInstance(url: String) = WebViewFragment(url)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.webview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpWebView()
    }

    private fun setUpWebView() {
        view?.let{
            this.webView = it.findViewById(R.id.fullscreen_webview)
            val webSettings = this.webView.settings
            webSettings.javaScriptEnabled = true

            this.webView.webViewClient = WebClientClass()
            Log.i("WebViewFragment", "Showing this url : $url")

//            val data = "&lt;iframe class=\"embedly-embed\" src=\"https://cdn.embedly.com/widgets/media.html?src=https%3A%2F%2Fgfycat.com%2Fifr%2Fessentialaccurateaffenpinscher&amp;display_name=Gfycat&amp;url=https%3A%2F%2Fgfycat.com%2Fessentialaccurateaffenpinscher&amp;image=https%3A%2F%2Fthumbs.gfycat.com%2FEssentialAccurateAffenpinscher-size_restricted.gif&amp;key=ed8fa8699ce04833838e66ce79ba05f1&amp;type=text%2Fhtml&amp;schema=gfycat\" width=\"600\" height=\"750\" scrolling=\"no\" title=\"Gfycat embed\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen=\"true\"&gt;&lt;/iframe&gt;"
            val data = "<iframe src='https://gfycat.com/ifr/EssentialAccurateAffenpinscher' frameborder='0' allow='autoplay' scrolling='no' width='640' height='844'></iframe>"
//            val imageUrl = "https://sewingheartdesign.com/wp-content/uploads/2020/07/cool-cat-26x16-1.jpg"
//            val data = "<img src=\"$imageUrl\" alt=\"HTML5 Icon\" width=\"128\" height=\"128\">"
//            val encoded = Base64.encodeToString(data.toByteArray(), Base64.NO_PADDING);

            val html = "<html><body>$data</body></html>"
            val htmlTest = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<blockquote class=\"imgur-embed-pub\" lang=\"en\" data-id=\"a/E14MRSj\" data-context=\"false\" ><a href=\"//imgur.com/a/E14MRSj\">Party&#39;s over time to put the bar away.</a></blockquote><script async src=\"//s.imgur.com/min/embed.js\" charset=\"utf-8\"></script>\n" +
                    "</body>\n" +
                    "</html>"
//            this.webView.loadDataWithBaseURL(null, htmlTest, "text/html", "utf-8", null);
//            this.webView.loadUrl("http://jamnoran.se/test.html")
            this.webView.loadUrl(url)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView.canGoBack()) {
            this.webView.goBack()
            return true
        }
        return false
    }
}
