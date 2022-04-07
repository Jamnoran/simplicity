package com.simplicity.simplicityaclientforreddit.ui.main

enum class CustomFormatType {
    BOLD, ITALIC, QUOTE, TITLE_1, TITLE_2, TITLE_3, NEW_SECTION, EMPTY_PARAGRAPH, AND_SIGN, NONE
}
class FormatIdentifier{
    companion object {
        const val BOLD = "**"
        const val BOLD_SECONDARY = "__"
        const val ITALIC = "*"
        const val ITALIC_SECONDARY = "_"
        const val NEW_SECTION = "&#x200B;"
        const val EMPTY_PARAGRAPH = "&amp;x200B;"
        const val QUOTE = "&gt;"
        const val QUOTE_END = "\n"
        const val TITLE_1 = "#"
        const val TITLE_2 = "##"
        const val TITLE_3 = "####"
        const val LINE_BREAK = "\n"
        const val AND_SIGN = "&amp;"
    }
}