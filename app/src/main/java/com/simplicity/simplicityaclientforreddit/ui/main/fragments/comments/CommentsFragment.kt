package com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseActivity
import com.simplicity.simplicityaclientforreddit.base.BaseAdapterFragment
import com.simplicity.simplicityaclientforreddit.databinding.CommentsFragmentBinding
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.Children
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.text.GetFormattedTextUseCase

class CommentsFragment(var subreddit: String, var postId: String, var author: String) : BaseAdapterFragment() {
    private val TAG: String = "CommentsFragment"
    lateinit var binding: CommentsFragmentBinding

    companion object {
        fun newInstance(subreddit: String, postId: String, author: String) = CommentsFragment(subreddit, postId, author)
    }

    private lateinit var viewModel: CommentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CommentsViewModel::class.java)

        viewModel.fetchComments(subreddit, postId)
        viewModel.comments().observe(requireActivity()) {
            observeComments(it)
        }
        viewModel.isFetching().observe(requireActivity()) { observeLoading(it) }
        (activity as BaseActivity).logFirebaseEvent("fragment_comments", TAG, "logged_in_false")
    }

    private fun observeLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }
    }

    private fun observeComments(comments: CommentResponse) {
        comments.commentResponseData?.let {
            val wrappedList = ArrayList<CommentViewHolder>()
            for (child in it.children) {
                wrappedList.add(CommentViewHolder(CommentViewData(child), viewModel, author))
//                addCommentInView(child, binding.commentsList)
//                addDivider(binding.commentsList)
            }

            submitList(wrappedList as ArrayList<Any>)
        }
    }

    private fun addDivider(commentsList: LinearLayout) {
        commentsList.addView(layoutInflater.inflate(R.layout.comment_divider, null))
    }

    private fun addCommentInView(comment: Children, parentView: LinearLayout) {
        comment.childrenData?.let {
            val commentLayout = layoutInflater.inflate(R.layout.comment_layout, null)
            commentLayout.findViewById<TextView>(R.id.author)?.let { authorView ->
                it.author?.let { author ->
                    authorView.visibility = View.VISIBLE
                    authorView.text = getString(R.string.comment_author, author)
                } ?: run {
                    authorView.visibility = View.GONE
                }
            }

            commentLayout.findViewById<TextView>(R.id.comment)?.let { commentBodyView ->
                it.body?.let { body ->
                    commentBodyView.setText(
                        GetFormattedTextUseCase(RedditPostListenerImpl(viewModel)).execute(body), TextView.BufferType.SPANNABLE
                    )
                } ?: run {
                    commentBodyView.text = getString(R.string.comment_deleted)
                }
            }

            it.repliesCustomParsed?.repliesData?.let { replies ->
                for (child in replies.children) {
                    addCommentInView(child, commentLayout.findViewById(R.id.child_comments))
                }
            }

            commentLayout.setOnLongClickListener {
                toggleExpandView(commentLayout)
                true
            }
            parentView.addView(commentLayout)
        }
    }

    private fun toggleExpandView(commentLayout: View) {
        commentLayout.findViewById<TextView>(R.id.comment)?.let { commentBodyView ->
            if (commentLayout.findViewById<LinearLayout>(R.id.child_comments).visibility == View.GONE) {
                commentBodyView.maxLines = 2048
                commentLayout.findViewById<LinearLayout>(R.id.child_comments).visibility = View.VISIBLE
            } else {
                commentBodyView.maxLines = 1
                commentLayout.findViewById<LinearLayout>(R.id.child_comments).visibility = View.GONE
            }
        }
    }
}
