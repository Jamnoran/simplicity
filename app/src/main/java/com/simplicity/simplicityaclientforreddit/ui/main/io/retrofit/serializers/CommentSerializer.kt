package com.simplicity.simplicityaclientforreddit.ui.main.io.retrofit.serializers

import android.util.Log
import com.google.gson.*
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.Children
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.CommentResponse
import com.simplicity.simplicityaclientforreddit.ui.main.models.external.responses.comments.Replies
import java.lang.reflect.Type
import kotlin.Throws

class CommentSerializer : JsonDeserializer<CommentResponse?> {
    private val TAG: String = "CommentSerializer"

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): CommentResponse? {
        var commentResponse = Gson().fromJson(json, CommentResponse::class.java)
        var isComment = false
        commentResponse.commentResponseData?.children?.get(0)?.let{
            if(it.kind.equals("t1")){
//                Log.i(TAG, "Comment to parse further: $it")
                isComment = true
            }
        }
        if(isComment){
            commentResponse = addRepliesToCommentObject(commentResponse, json)

//            Log.i(TAG, "commentResponse with custom parsed $commentResponse")
        }
        return commentResponse
    }

    private fun addRepliesToCommentObject(commentResponse: CommentResponse, json: JsonElement): CommentResponse? {
        val commentResponseJsonObject = json.asJsonObject
        val data = commentResponseJsonObject["data"].asJsonObject
        val children = data["children"].asJsonArray

        children.forEachIndexed { index, child ->
            val childFromResponse = commentResponse.commentResponseData?.children?.get(index)
            goThrough(childFromResponse, child)
        }
        return commentResponse
    }

    private fun addRepliesToRepliesObject(repliesObject: Replies, repliesJsonObject: JsonObject) {
        val data = repliesJsonObject["data"].asJsonObject
        val children = data["children"].asJsonArray
        children.forEachIndexed { index, child ->
            val childFromResponse = repliesObject.repliesData?.children?.get(index)
            goThrough(childFromResponse, child)
        }
    }

    private fun goThrough(childFromResponse: Children?, child: JsonElement) {
        val childData = child.asJsonObject["data"].asJsonObject
        var replies : JsonObject? = null
        try {
            replies = childData["replies"].asJsonObject
        } catch (e: Exception) {
        }
        replies?.let{
//            Log.i(TAG, "repliesObject: $replies")
            // Found child that has replies.
            val repliesObject = Gson().fromJson(replies, Replies::class.java)
            repliesObject.repliesData?.children
            addRepliesToRepliesObject(repliesObject, it)
//            Log.i(TAG, "SuccessFull Gson parsed replies : $repliesObject")
            childFromResponse?.childrenData?.repliesCustomParsed = repliesObject
        }
    }

}