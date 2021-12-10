package com.example.pdp_ai.ui.utils

import org.json.JSONArray

object BotResponse {

    fun basicResponses(question: String, jsonArray: JSONArray, language: String): String {

        val question = question.lowercase()
        for (i in 0 until jsonArray.length()) {
            var isHave = true
            val answerObj = jsonArray.getJSONObject(i).getJSONArray("keys")
            for (j in 0 until answerObj.length()) {
                if (!question.contains(answerObj[j].toString())) {
                    isHave = false
                    break
                }
            }
            if (isHave) {
                return jsonArray.getJSONObject(i).get("answer").toString()
            }
        }

        return when (language) {
            "English" -> "Sorry I don't understand the question.\nYou can get more information via contacting  +998 97 777 47 47"
            "Uzbek" -> "Uzr savolingizga tushunmadim.\n+998 97 777 47 47  telefon raqamiga bog'lanib ba'tafsil ma'lumot olishingiz mumkin"
            else -> {"Error in Language / Tilda xatolik sodir bo'ldi"}
        }

    }
}