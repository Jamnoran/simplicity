package com.simplicity.simplicityaclientforreddit.ui.main.usecases.text

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.core.text.bold
import androidx.core.text.italic
import com.simplicity.simplicityaclientforreddit.ui.main.CustomFormatType
import com.simplicity.simplicityaclientforreddit.ui.main.FormatIdentifier
import com.simplicity.simplicityaclientforreddit.ui.main.listeners.RedditPostListener

class GetFormattedTextUseCase(val listener: RedditPostListener) {
    fun execute(it: String): SpannableStringBuilder {
        return main(it)
//        return main("Test *italic* **bold** \n#Title \n##Title2\n####Title3\nnew line \n&gt;This is quote \n&#x200B;Edit: Testing a little")
//        return main("&gt;This is a quote with **bold** in it. \n\n####Title\nThis is new line")
//        return main("Link test https://www.google.com/ and another test http://www.google.se \n also some more examples:\n\n/u/Jamonran \n\nu/Jamonran\n\n/r/funny")
    }

    fun main(inputText: String): SpannableStringBuilder {
        val finalResult = SpannableStringBuilder("")
        val replacedText = replaceAllInstances(inputText)
        loopThroughTextAndAdd(finalResult, replacedText)
        return finalResult
    }

    private fun replaceAllInstances(inputText: String): String {
        var updatedText = replaceWithTemporaryRemoveEncapsulated(inputText, "&gt;", ">")
        updatedText = replaceWithTemporaryRemoveEncapsulated(updatedText, "&lt;", "<")
        updatedText = replaceWithTemporaryRemoveEncapsulated(updatedText, "&amp;", "&")
        updatedText = replaceWithTemporaryRemoveEncapsulated(updatedText, ">", "|   ")
        return updatedText
    }

    private fun replaceWithTemporaryRemoveEncapsulated(
        inputText: String,
        oldValue: String,
        newValue: String
    ): String {
        val specialEncapsulated = "≥ú{Š"
        val encapsulated = inputText.replace("\$newValue", specialEncapsulated)
        val replaceWithNewValue = encapsulated.replace(oldValue, newValue)
        return replaceWithNewValue.replace(specialEncapsulated, newValue)
    }

    private fun loopThroughTextAndAdd(finalResult: SpannableStringBuilder, text: String) {
        var positionAtWhereLoopShouldStartAgain = -1
        for (currentPosition in text.indices) {
            if (positionAtWhereLoopShouldStartAgain == -1 || positionAtWhereLoopShouldStartAgain == currentPosition) {
                positionAtWhereLoopShouldStartAgain = -1
                val positionOfEndTag = checkIfFormatAndReturnPostFixPosition(text.substring(currentPosition, text.length))
                val format = GetFormatUseCase().execute(text.substring(currentPosition, text.length))
                if (positionOfEndTag == -1) { // No formatting, just add character
                    finalResult.append(text[currentPosition])
                } else if (isPrefixFormat(format)) { // Formatting found that is only prefix, add a prefix then continue with rest
                    positionAtWhereLoopShouldStartAgain = currentPosition + getFormatIdentifierLength(format)
                    appendPreFix(finalResult, format)
                } else { // Formatting found, find post-fix and format the text
                    positionAtWhereLoopShouldStartAgain = currentPosition + positionOfEndTag + getFormatPostFixLength(format)
                    val startPositionAfterPreFix = currentPosition + getFormatIdentifierLength(format)
                    val indexOfEndOfFormattedStringWithoutPostFix = currentPosition + positionOfEndTag
                    if (startPositionAfterPreFix < indexOfEndOfFormattedStringWithoutPostFix) {
                        val substringToFormat = text.substring(startPositionAfterPreFix, indexOfEndOfFormattedStringWithoutPostFix)
//                        Log.i("GetFormattedTextUseCase", "Formatting string [$substringToFormat] with format [$format]")
                        formatText(finalResult, format, substringToFormat)
                    }
                }
            }
        }
    }

    private fun isPrefixFormat(format: CustomFormatType): Boolean {
        if (format == CustomFormatType.EMPTY_PARAGRAPH) {
            return true
        }
        return false
    }

    private fun appendPreFix(finalResult: SpannableStringBuilder, format: CustomFormatType) {
        when (format) {
            CustomFormatType.EMPTY_PARAGRAPH -> {
                finalResult.append("")
            }
            CustomFormatType.NEW_SECTION -> {
                finalResult.append("\n\n\n")
            }
            else -> { }
        }
    }

    private fun getFormatIdentifierLength(format: CustomFormatType): Int {
        return when (format) {
            CustomFormatType.NONE -> 0
            CustomFormatType.BOLD -> FormatIdentifier.BOLD.length
            CustomFormatType.ITALIC -> FormatIdentifier.ITALIC.length
            CustomFormatType.TITLE_1 -> FormatIdentifier.TITLE_1.length
            CustomFormatType.TITLE_2 -> FormatIdentifier.TITLE_2.length
            CustomFormatType.TITLE_3 -> FormatIdentifier.TITLE_3.length
            CustomFormatType.EMPTY_PARAGRAPH -> FormatIdentifier.EMPTY_PARAGRAPH.length
            CustomFormatType.LINK_HTTP,
            CustomFormatType.LINK_HTTPS,
            CustomFormatType.LINK_USER,
            CustomFormatType.LINK_USER_SECONDARY,
            CustomFormatType.LINK_REDDIT,
            CustomFormatType.LINK_REDDIT_SECONDARY -> 0
            CustomFormatType.NEW_SECTION -> FormatIdentifier.NEW_SECTION.length
        }
    }

    private fun getFormatPostFixLength(format: CustomFormatType): Int {
        return when (format) {
            CustomFormatType.NONE -> 0
            CustomFormatType.BOLD -> FormatIdentifier.BOLD.length
            CustomFormatType.ITALIC -> FormatIdentifier.ITALIC.length
            CustomFormatType.NEW_SECTION,
            CustomFormatType.EMPTY_PARAGRAPH -> 0
            CustomFormatType.LINK_HTTP,
            CustomFormatType.LINK_HTTPS,
            CustomFormatType.LINK_USER,
            CustomFormatType.LINK_USER_SECONDARY,
            CustomFormatType.LINK_REDDIT,
            CustomFormatType.LINK_REDDIT_SECONDARY -> 0
            CustomFormatType.TITLE_1,
            CustomFormatType.TITLE_2,
            CustomFormatType.TITLE_3 -> FormatIdentifier.LINE_BREAK.length - 1
        }
    }

    private fun checkIfFormatAndReturnPostFixPosition(subString: String): Int {
        val format = GetFormatUseCase().execute(subString)
        val preFixLength = getFormatIdentifierLength(format)
        val subStringWithoutPreFix = subString.substring(preFixLength, subString.length)
        val indexOfPostFix: Int = when (format) {
            CustomFormatType.BOLD -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.BOLD, FormatIdentifier.BOLD_SECONDARY))
            }
            CustomFormatType.ITALIC -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.ITALIC, FormatIdentifier.ITALIC_SECONDARY))
            }
            CustomFormatType.EMPTY_PARAGRAPH -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.EMPTY_PARAGRAPH))
            }
            CustomFormatType.LINK_HTTP,
            CustomFormatType.LINK_HTTPS,
            CustomFormatType.LINK_USER,
            CustomFormatType.LINK_USER_SECONDARY,
            CustomFormatType.LINK_REDDIT,
            CustomFormatType.LINK_REDDIT_SECONDARY -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.SPACE, FormatIdentifier.LINE_BREAK))
            }
            CustomFormatType.TITLE_3,
            CustomFormatType.TITLE_2,
            CustomFormatType.TITLE_1 -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.LINE_BREAK))
            }
            CustomFormatType.NONE -> -1
            else -> -1
        }
        return preFixLength + indexOfPostFix // Need to add prefix length again before returning since we remove it when checking for position
    }

    private fun getPostFixPosition(subStringWithoutPreFix: String, postFixes: Array<String>): Int {
        var positionOfPostFix = -1
        for (postFix in postFixes) {
            val postfixPos = subStringWithoutPreFix.indexOf(postFix)
            if (positionOfPostFix == -1 || (postfixPos != -1 && postfixPos < positionOfPostFix)) {
                positionOfPostFix = postfixPos
            }
        }
        if (positionOfPostFix == -1) {
            positionOfPostFix = subStringWithoutPreFix.length
        }
        return positionOfPostFix
    }

    private fun formatText(
        finalResult: SpannableStringBuilder,
        format: CustomFormatType,
        substring: String
    ) {
        when (format) {
            CustomFormatType.BOLD -> {
                getBold(finalResult, substring)
            }
            CustomFormatType.ITALIC -> {
                getItalic(finalResult, substring)
            }
            CustomFormatType.TITLE_3 -> {
                getTitle(finalResult, substring, 1.4f)
            }
            CustomFormatType.TITLE_2 -> {
                getTitle(finalResult, substring, 1.7f)
            }
            CustomFormatType.TITLE_1 -> {
                getTitle(finalResult, substring, 2.2f)
            }
            CustomFormatType.LINK_HTTP,
            CustomFormatType.LINK_HTTPS,
            CustomFormatType.LINK_REDDIT,
            CustomFormatType.LINK_REDDIT_SECONDARY,
            CustomFormatType.LINK_USER,
            CustomFormatType.LINK_USER_SECONDARY, -> {
                getLink(finalResult, substring, format)
            }
            else -> {
                finalResult.append(substring)
            }
        }
    }

    private fun getTitle(finalResult: SpannableStringBuilder, text: String, size: Float) {
//        finalResult.append(getColor(text, Color.RED))
        finalResult.append(getSize(text, size))
    }

    private fun getLink(finalResult: SpannableStringBuilder, text: String, format: CustomFormatType) {
        finalResult.append(text)
        val startPos = finalResult.length - text.length
        val endPos = finalResult.length
        val myClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                when (format) {
                    CustomFormatType.LINK_HTTP,
                    CustomFormatType.LINK_HTTPS -> {
                        listener.directLinkClicked(text)
                    }
                    CustomFormatType.LINK_USER -> {
                        listener.directAuthorClicked(text.replace("/u/", ""))
                    }
                    CustomFormatType.LINK_USER_SECONDARY -> {
                        listener.directAuthorClicked(text.replace("u/", ""))
                    }
                    CustomFormatType.LINK_REDDIT -> {
                        listener.directRedditClicked(text.replace("/r/", ""))
                    }
                    CustomFormatType.LINK_REDDIT_SECONDARY -> {
                        listener.directRedditClicked(text.replace("r/", ""))
                    }
                    else -> {}
                }
            }
        }
        finalResult.setSpan(myClickableSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun getBold(finalResult: SpannableStringBuilder, text: String) {
        finalResult.bold { append(text) }
    }

    private fun getItalic(finalResult: SpannableStringBuilder, text: String) {
        finalResult.italic { append(text) }
    }

    private fun getSize(text: String, size: Float): SpannableString {
        val whiteSpannable = SpannableString(text)
        whiteSpannable.setSpan(RelativeSizeSpan(size), 0, text.length, 0)
        return whiteSpannable
    }

    private fun getColor(text: String, color: Int): SpannableString {
        val whiteSpannable = SpannableString(text)
        whiteSpannable.setSpan(ForegroundColorSpan(color), 0, text.length, 0)
        return whiteSpannable
    }
}
