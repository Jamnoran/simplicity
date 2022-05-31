package com.simplicity.simplicityaclientforreddit.ui.main.fragments.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseActivity
import com.simplicity.simplicityaclientforreddit.databinding.DetailFragmentBinding
import com.simplicity.simplicityaclientforreddit.base.BaseFragment
import com.simplicity.simplicityaclientforreddit.base.SingleFragmentActivity
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews.RedditMedia
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost

class DetailFragment : BaseFragment() {
    private val TAG: String = "DetailFragment"

    lateinit var binding: DetailFragmentBinding

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DetailFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

//        viewModel.parsePost(resources.openRawResource(R.raw.post_raw_video2).bufferedReader().use { it.readText() })
//        viewModel.parsePost(resources.openRawResource(R.raw.post_raw_rich_video).bufferedReader().use { it.readText() })
//        viewModel.parsePost(resources.openRawResource(R.raw.post_hosted_video).bufferedReader().use { it.readText() })
        viewModel.parsePost(resources.openRawResource(R.raw.post_desc).bufferedReader().use { it.readText() })
        viewModel.post().observe(requireActivity(), {
            observeRedditPost(it)
//            test(it)
        }
        )
        viewModel.webViewActivityClicked().observe(requireActivity(),  { observeWebViewActivity(it)})
//        (activity as BaseActivity).logFirebaseEvent("fragment_detail", TAG, "logged_in_false")
    }


    private fun test(it: RedditPost) {

//        val command = "&lt;!-- SC_OFF --&gt;&lt;div class=\"md\"&gt;&lt;p&gt;The other day I had to shop at the sketchy grocery store. I usually don&amp;#39;t go there but I was desperate for some hard to find ingredients. &lt;/p&gt;\n\n&lt;p&gt;When I was walking down an aisle I noticed out of the corner of my eye this guy walking behind me. I didn&amp;#39;t think much of it, that&amp;#39;s how grocery stores work, and thought maybe he was just walking to another aisle or something ( he clearly wasn&amp;#39;t shopping that aisle).&lt;/p&gt;\n\n&lt;p&gt;But then he followed me down through the next two aisles. It was clear at that point he was following me and not shopping. He was staying at the same distance even though I sped up. &lt;/p&gt;\n\n&lt;p&gt;I remembered reading on a tip on reddit to stare down people who are following you, because they will back off  usually. I always thought that was kind of silly, but I thought what the heck. &lt;/p&gt;\n\n&lt;p&gt;I stopped abruptly and whipped around and stared this dude down. He quickly turned around and left. &lt;/p&gt;\n\n&lt;p&gt;Anyways I hope this trick helps someone else out there because it worked for me!  Never knew I&amp;#39;d have to use it. &lt;/p&gt;\n\n&lt;p&gt;Also: I&amp;#39;m not generally a paranoid person at all. Pretty comfortable most times in public, but the vibes from this guy were really off.&lt;/p&gt;\n\n&lt;p&gt;Edit: to those being unnecessarily skeptical. He was Def following me. That&amp;#39;s why I checked by walking into multiple aisles and I was going fast too. Also if me turning around had just scared him, his reaction wouldn&amp;#39;t have been as weird as it was. Either way, better be safe than sorry.&lt;/p&gt;\n&lt;/div&gt;&lt;!-- SC_ON --&gt;"
        val command = "<b>test</b></br></br>test2"
//        binding.redditMedia.redditMedia.redditDescriptionLayout.redditTextContent.visibility = View.VISIBLE
//        binding.redditMedia.redditMedia.redditDescriptionLayout.redditTextContent.text = Html.fromHtml(command) // , Html.FROM_HTML_MODE_COMPACT
        binding.redditMedia.redditMedia.redditWebLayout.redditWebview.visibility = View.VISIBLE
        binding.redditMedia.redditMedia.redditWebLayout.redditWebview.settings.javaScriptEnabled = true
        binding.redditMedia.redditMedia.redditWebLayout.redditWebview.loadData(command, "text/html; charset=utf-8", "UTF-8");
//        val intent = Intent(Intent.ACTION_WEB_SEARCH)
//        intent.setClassName(
//            "com.google.android.googlequicksearchbox",
//            "com.google.android.googlequicksearchbox.SearchActivity"
//        )
//        intent.putExtra("query", command)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK //necessary if launching from Service
//
//        requireContext().startActivity(intent)
    }

    private fun sendToBrowser(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun observeRedditPost(post: RedditPost) {
        view?.let{
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            RedditMedia(post, RedditPostListenerImpl(viewModel)).init(binding.redditMedia)
        }
        if(post.data.postHint != "link"){
            view?.findViewById<TextView>(R.id.reddit_title)?.text = post.data.title
        }else{
            view?.findViewById<TextView>(R.id.reddit_title)?.visibility = View.GONE
        }
    }


    private fun observeWebViewActivity(it: String) {
        Log.i(TAG, "Starting webview with url $it")
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        startActivity(browserIntent)
    }

}