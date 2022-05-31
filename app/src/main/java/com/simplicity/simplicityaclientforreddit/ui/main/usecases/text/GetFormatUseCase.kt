package com.simplicity.simplicityaclientforreddit.ui.main.usecases.text

import com.simplicity.simplicityaclientforreddit.ui.main.CustomFormatType
import com.simplicity.simplicityaclientforreddit.ui.main.FormatIdentifier

class GetFormatUseCase {

    fun execute(subString: String): CustomFormatType {
        if(subString.startsWith(FormatIdentifier.BOLD)){
            return CustomFormatType.BOLD
        }
        if(subString.startsWith(FormatIdentifier.ITALIC)){
            return CustomFormatType.ITALIC
        }
        if(subString.startsWith(FormatIdentifier.EMPTY_PARAGRAPH)){
            return CustomFormatType.EMPTY_PARAGRAPH
        }
        if(subString.startsWith(FormatIdentifier.NEW_SECTION)){
            return CustomFormatType.NEW_SECTION
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
        if(subString.startsWith(FormatIdentifier.LINK_HTTP)){
            return CustomFormatType.LINK_HTTP
        }
        if(subString.startsWith(FormatIdentifier.LINK_HTTPS)){
            return CustomFormatType.LINK_HTTPS
        }
        if(subString.startsWith(FormatIdentifier.LINK_REDDIT)){
            return CustomFormatType.LINK_REDDIT
        }
        if(subString.startsWith(FormatIdentifier.LINK_REDDIT_SECONDARY)){
            return CustomFormatType.LINK_REDDIT_SECONDARY
        }
        if(subString.startsWith(FormatIdentifier.LINK_USER)){
            return CustomFormatType.LINK_USER
        }
        if(subString.startsWith(FormatIdentifier.LINK_USER_SECONDARY)){
            return CustomFormatType.LINK_USER_SECONDARY
        }
        return CustomFormatType.NONE
    }
}