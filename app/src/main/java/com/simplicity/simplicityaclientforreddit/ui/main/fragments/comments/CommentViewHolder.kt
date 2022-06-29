package com.simplicity.simplicityaclientforreddit.ui.main.fragments.comments

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.simplicity.simplicityaclientforreddit.R
import com.simplicity.simplicityaclientforreddit.base.BaseViewHolderItem
import com.simplicity.simplicityaclientforreddit.ui.main.fragments.list.util.RedditPostListenerImpl
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.ChildrenData
import com.simplicity.simplicityaclientforreddit.ui.main.usecases.text.GetFormattedTextUseCase
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class CommentViewHolder(var wrapper: CommentViewData, val viewModel: CommentsViewModel, val author: String) : BaseViewHolderItem(wrapper) {
    private val TAG = "CommentViewHolder"

    override fun getLayout() = R.layout.comment_layout

    override fun bind(itemView: View) {
        view = itemView
        constructComment(wrapper.data.childrenData, view)
    }

    private fun constructComment(child: ChildrenData?, view: View) {
        child?.let {
            setAuthor(child, view)
            setComment(child, view)
            setReplies(child, view)
            setToggle(child, view)
            setUpBottomSection(child, view)
        }
    }

    private fun setAuthor(child: ChildrenData, itemView: View) {
        // Author name
        itemView.findViewById<TextView>(R.id.author)?.let { authorView ->
            child.author?.let { author ->
                authorView.visibility = View.VISIBLE
                string(R.string.comment_deleted)
                authorView.text = context()?.getString(R.string.comment_author, author)
            } ?: run {
                authorView.visibility = View.GONE
            }
        }
        // Is OP
        itemView.findViewById<TextView>(R.id.comment_op)?.let {
            if (child.author == author) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
        // Posted
        itemView.findViewById<TextView>(R.id.comment_posted)?.let {
            child.createdUtc?.let{ time ->
                val prettyTime = PrettyTime(Locale.getDefault())
                val ago: String = prettyTime.format(Date((time * 1000)))
                it.text = ago
            }
        }
    }

    private fun setComment(child: ChildrenData, itemView: View) {
        itemView.findViewById<TextView>(R.id.comment)?.let { commentBodyView ->
            child.body?.let { body ->
                commentBodyView.setText(
                    GetFormattedTextUseCase(RedditPostListenerImpl(viewModel)).execute(body), TextView.BufferType.SPANNABLE
                )
            } ?: run {
                commentBodyView.text = string(R.string.comment_deleted)
            }
        }
    }

    private fun setReplies(child: ChildrenData, itemView: View) {
        child.repliesCustomParsed?.repliesData?.let { replies ->
            for (reply in replies.children) {
                val layoutInflater = LayoutInflater.from(context())
                val commentLayout = layoutInflater.inflate(R.layout.comment_layout, null)
                itemView.findViewById<LinearLayout>(R.id.child_comments).addView(commentLayout)
                constructComment(reply.childrenData, commentLayout)
            }
        }
    }

    private fun setToggle(child: ChildrenData, view: View) {
        view.setOnLongClickListener {
            toggleExpandView(view)
            true
        }
    }

    private fun setUpBottomSection(child: ChildrenData, itemView: View) {
        itemView.findViewById<TextView>(R.id.hide_sub).visibility = View.GONE
        itemView.findViewById<ImageView>(R.id.share_button).visibility = View.GONE
        itemView.findViewById<ImageView>(R.id.chat_icon).visibility = View.GONE
        itemView.findViewById<TextView>(R.id.comments).visibility = View.GONE
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
