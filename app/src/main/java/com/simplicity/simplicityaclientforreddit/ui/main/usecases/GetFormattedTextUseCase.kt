package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.italic
import com.simplicity.simplicityaclientforreddit.ui.main.CustomFormatType
import com.simplicity.simplicityaclientforreddit.ui.main.FormatIdentifier
import android.text.style.ForegroundColorSpan

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log

class GetFormattedTextUseCase {
    fun execute(it: String): SpannableStringBuilder {
        return main(it)
//        return main("Test *italic* **bold** \n#Title \n##Title2\n####Title3\nnew line \n&gt;This is quote \n&#x200B;Edit: Testing a little")
//        return main("&gt;This is a quote with **bold** in it. \n\n####Title\nThis is new line")
    }
        // What is &amp;x200B;

    fun main(testText: String): SpannableStringBuilder{
        val finalResult = SpannableStringBuilder("")
        loopThroughTextAndAdd(finalResult, testText)
        return finalResult
    }

    private fun loopThroughTextAndAdd(finalResult: SpannableStringBuilder, text: String) {
        var positionAtWhereLoopShouldStartAgain = -1
        for(currentPosition in text.indices){
            if(positionAtWhereLoopShouldStartAgain == -1 || positionAtWhereLoopShouldStartAgain == currentPosition){
                positionAtWhereLoopShouldStartAgain = -1
                val positionOfEndTag = checkIfFormatAndReturnPostFixPosition(text.substring(currentPosition, text.length))
                val format = getFormat(text.substring(currentPosition, text.length))
                if(positionOfEndTag == -1){ // No formatting, just add character
                    finalResult.append(text[currentPosition])
                } else if(isPrefixFormat(format)){ // Formatting found that is only prefix, add a prefix then continue with rest
                        positionAtWhereLoopShouldStartAgain = currentPosition + getFormatIdentifierLength(format)
                        appendPreFix(finalResult, format)
                } else {// Formatting found, find post-fix and format the text
                    positionAtWhereLoopShouldStartAgain = currentPosition + positionOfEndTag + getFormatPostFixLength(format)
                    val startPositionAfterPreFix = currentPosition + getFormatIdentifierLength(format)
                    val indexOfEndOfFormattedStringWithoutPostFix = currentPosition + positionOfEndTag
                    if (startPositionAfterPreFix < indexOfEndOfFormattedStringWithoutPostFix) {
                        val substringToFormat = text.substring(startPositionAfterPreFix, indexOfEndOfFormattedStringWithoutPostFix)
                        Log.i("GetFormattedTextUseCase", "Formatting string [$substringToFormat] with format [$format]")
                        formatText(finalResult, format, substringToFormat)
                    }
                }
            }
        }
    }

    private fun isPrefixFormat(format: CustomFormatType): Boolean {
        if(format == CustomFormatType.QUOTE ||
            format == CustomFormatType.EMPTY_PARAGRAPH ||
            format == CustomFormatType.AND_SIGN){
            return true
        }
        return false
    }

    private fun appendPreFix(finalResult: SpannableStringBuilder, format: CustomFormatType) {
        when (format) {
            CustomFormatType.QUOTE ->{
                finalResult.append("    ")
            }
            CustomFormatType.EMPTY_PARAGRAPH ->{
                finalResult.append("")
            }
            CustomFormatType.AND_SIGN ->{
                finalResult.append("&")
            }
            CustomFormatType.NEW_SECTION ->{
                finalResult.append("\n\n\n")
            }
            else -> { }
        }
    }

    private fun getFormatIdentifierLength(format: CustomFormatType): Int {
        return when(format){
            CustomFormatType.NONE -> 0
            CustomFormatType.BOLD -> FormatIdentifier.BOLD.length
            CustomFormatType.ITALIC -> FormatIdentifier.ITALIC.length
            CustomFormatType.QUOTE -> FormatIdentifier.QUOTE.length
            CustomFormatType.TITLE_1 -> FormatIdentifier.TITLE_1.length
            CustomFormatType.TITLE_2 -> FormatIdentifier.TITLE_2.length
            CustomFormatType.TITLE_3 -> FormatIdentifier.TITLE_3.length
            CustomFormatType.EMPTY_PARAGRAPH -> FormatIdentifier.EMPTY_PARAGRAPH.length
            CustomFormatType.NEW_SECTION -> FormatIdentifier.NEW_SECTION.length
            CustomFormatType.AND_SIGN -> FormatIdentifier.AND_SIGN.length
        }
    }

    private fun getFormatPostFixLength(format: CustomFormatType): Int {
        return when(format){
            CustomFormatType.NONE -> 0
            CustomFormatType.BOLD -> FormatIdentifier.BOLD.length
            CustomFormatType.ITALIC -> FormatIdentifier.ITALIC.length
            CustomFormatType.QUOTE -> FormatIdentifier.QUOTE_END.length
            CustomFormatType.NEW_SECTION,
            CustomFormatType.EMPTY_PARAGRAPH -> 0
            CustomFormatType.AND_SIGN -> 0
            CustomFormatType.TITLE_1,
            CustomFormatType.TITLE_2,
            CustomFormatType.TITLE_3 -> FormatIdentifier.LINE_BREAK.length - 1
        }
    }

    private fun checkIfFormatAndReturnPostFixPosition(subString: String): Int {
        val format = getFormat(subString)
        val preFixLength = getFormatIdentifierLength(format)
        val subStringWithoutPreFix = subString.substring(preFixLength, subString.length)
        val indexOfSubFix: Int = when(format) {
            CustomFormatType.BOLD -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.BOLD, FormatIdentifier.BOLD_SECONDARY))
            }
            CustomFormatType.ITALIC -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.ITALIC))
            }
            CustomFormatType.EMPTY_PARAGRAPH -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.EMPTY_PARAGRAPH))
            }
            CustomFormatType.AND_SIGN -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.AND_SIGN))
            }
            CustomFormatType.TITLE_3,
            CustomFormatType.TITLE_2,
            CustomFormatType.TITLE_1 -> {
                getPostFixPosition(subStringWithoutPreFix, arrayOf(FormatIdentifier.LINE_BREAK))
            }
            CustomFormatType.NONE -> -1
            else -> -1
        }
        return preFixLength + indexOfSubFix // Need to add prefix length again before returning since we remove it when checking for position
    }

    private fun getPostFixPosition(subStringWithoutPreFix: String, postFixes: Array<String>): Int {
        var positionOfPostFix = -1
        for(postFix in postFixes){
            val postfixPos = subStringWithoutPreFix.indexOf(postFix)
            if(positionOfPostFix == -1 || (postfixPos != -1 && postfixPos < positionOfPostFix)){
                positionOfPostFix = postfixPos
            }
        }
        if(positionOfPostFix == -1){
            positionOfPostFix = subStringWithoutPreFix.length
        }
        return positionOfPostFix
    }

    private fun formatText(
        finalResult: SpannableStringBuilder,
        format: CustomFormatType,
        substring: String
    ){
        when (format) {
            CustomFormatType.BOLD -> {
                getBold(finalResult, substring)
            }
            CustomFormatType.ITALIC ->{
                getItalic(finalResult, substring)
            }
            CustomFormatType.TITLE_3 ->{
                getTitle(finalResult, substring, 1.4f)
            }
            CustomFormatType.TITLE_2 ->{
                getTitle(finalResult, substring, 1.7f)
            }
            CustomFormatType.TITLE_1 ->{
                getTitle(finalResult, substring, 2.2f)
            }
            else -> {
                finalResult.append(substring)
            }
        }
    }

    private fun getFormat(subString: String): CustomFormatType {
        if(subString.startsWith(FormatIdentifier.BOLD)){
            return CustomFormatType.BOLD
        }
        if(subString.startsWith(FormatIdentifier.ITALIC)){
            return CustomFormatType.ITALIC
        }
        if(subString.startsWith(FormatIdentifier.QUOTE)){
            return CustomFormatType.QUOTE
        }
        if(subString.startsWith(FormatIdentifier.EMPTY_PARAGRAPH)){
            return CustomFormatType.EMPTY_PARAGRAPH
        }
        if(subString.startsWith(FormatIdentifier.NEW_SECTION)){
            return CustomFormatType.NEW_SECTION
        }
        if(subString.startsWith(FormatIdentifier.AND_SIGN)){
            return CustomFormatType.AND_SIGN
        }
        if(subString.startsWith(FormatIdentifier.TITLE_3)){
            return CustomFormatType.TITLE_3
        }
        if(subString.startsWith(FormatIdentifier.TITLE_2)){
            return CustomFormatType.TITLE_2
        }
        if(subString.startsWith(FormatIdentifier.TITLE_1)){
            return CustomFormatType.TITLE_1
        }
        return CustomFormatType.NONE
    }

    private fun getBold(finalResult: SpannableStringBuilder, text: String) {
        finalResult.bold { append(text) }
    }

    private fun getTitle(finalResult: SpannableStringBuilder, text: String, size: Float) {
//        finalResult.append(getColor(text, Color.RED))
        finalResult.append(getSize(text, size))
    }

    private fun getItalic(finalResult: SpannableStringBuilder, text: String){
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