package com.simplicity.simplicityaclientforreddit.ui.main

enum class CustomFormatType {
    BOLD, ITALIC, TITLE_1, TITLE_2, TITLE_3, NEW_SECTION, EMPTY_PARAGRAPH, LINK_HTTP, LINK_HTTPS, LINK_USER, LINK_USER_SECONDARY, LINK_REDDIT, LINK_REDDIT_SECONDARY, NONE
}
class FormatIdentifier{
    companion object {
        const val BOLD = "**"
        const val BOLD_SECONDARY = "__"
        const val ITALIC = "*"
        const val ITALIC_SECONDARY = "_"
        const val NEW_SECTION = "&#x200B;"
        const val EMPTY_PARAGRAPH = "#x200B;"
        const val LINK_HTTP = "http://"
        const val LINK_HTTPS = "https://"
        const val LINK_USER = "/u/"
        const val LINK_USER_SECONDARY = "u/"
        const val LINK_REDDIT = "/r/"
        const val LINK_REDDIT_SECONDARY = "r/"
        const val FULL_WIDTH_LINE = "***"
//        const val QUOTE = ">"
//        const val QUOTE_NESTED = ">>"
//        const val QUOTE_DOUBLE_NESTED = ">>>"
//        const val QUOTE_END = "\n"
        const val TITLE_1 = "#"
        const val TITLE_2 = "##"
        const val TITLE_3 = "####"
        const val LINE_BREAK = "\n"
        const val SPACE = " "
    }
}