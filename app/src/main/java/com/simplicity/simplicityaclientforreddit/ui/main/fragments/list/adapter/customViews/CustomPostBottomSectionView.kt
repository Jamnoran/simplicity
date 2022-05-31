package com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.adapter.customViews

import android.widget.TextView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.databinding.RedditPostBinding
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.posts.RedditPost
import java.text.NumberFormat

class CustomPostBottomSectionView(val binding: RedditPostBinding, val  post: RedditPost, var listener: RedditPostListener) {
    var ownVote = 0
    fun init() {
//        setColorOfTextView(binding.bottomLayout.downVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_de_active_color))
        binding.bottomLayout.downVoteButton.setImageResource(R.drawable.down_arrow_disabled)
//        setColorOfTextView(binding.bottomLayout.upVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_de_active_color))
        binding.bottomLayout.upVoteButton.setImageResource(R.drawable.up_arrow_disabled)
        showVoteNumber()
        showCommentNumbers()
        setUpListeners()
    }

    private fun showCommentNumbers() {
        binding.bottomLayout.comments.text = binding.root.context.getString(R.string.comment_counter, NumberFormat.getInstance().format(post.data.numComments))
    }

    private fun setUpListeners() {
        binding.bottomLayout.let{
            it.downVoteButton.setOnClickListener { downVote() }
            it.upVoteButton.setOnClickListener { upVote() }
            it.comments.setOnClickListener { listener.commentsClicked(post) }
        }
    }

    private fun upVote() {
        ownVote = +1
//        setColorOfTextView(binding.bottomLayout.downVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_de_active_color))
//        setColorOfTextView(binding.bottomLayout.upVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_active_color))
        binding.bottomLayout.downVoteButton.setImageResource(R.drawable.down_arrow_disabled)
        binding.bottomLayout.upVoteButton.setImageResource(R.drawable.up_arrow_normal)
        showVoteNumber()
        listener.voteUp(post)
    }

    private fun downVote() {
        ownVote = -1
//        setColorOfTextView(binding.bottomLayout.downVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_active_color))
//        setColorOfTextView(binding.bottomLayout.upVote, ContextCompat.getColor(binding.root.context, R.color.post_vote_de_active_color))
        binding.bottomLayout.downVoteButton.setImageResource(R.drawable.down_arrow_normal)
        binding.bottomLayout.upVoteButton.setImageResource(R.drawable.up_arrow_disabled)
        listener.voteDown(post)
        showVoteNumber()
    }

    private fun showVoteNumber() {
        val votes = (post.data.ups - post.data.downs) + ownVote
        binding.bottomLayout.votes.text = NumberFormat.getInstance().format(votes)
    }

    private fun setColorOfTextView(it: TextView, color: Int) {
        it.setTextColor(color)
    }

}
