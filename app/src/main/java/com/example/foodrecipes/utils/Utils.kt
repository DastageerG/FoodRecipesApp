package com.example.foodrecipes.utils

import org.jsoup.Jsoup

object Utils
{

    fun parseHtmlTags(text: String?) : String = Jsoup.parse(text.toString()).text()
}