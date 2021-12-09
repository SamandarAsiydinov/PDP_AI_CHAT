package com.example.pdp_ai.ui.utils

import com.example.pdp_ai.ui.utils.Constants.ABOUT
import com.example.pdp_ai.ui.utils.Constants.CER
import com.example.pdp_ai.ui.utils.Constants.EXIT
import com.example.pdp_ai.ui.utils.Constants.KURS
import com.example.pdp_ai.ui.utils.Constants.KURSTU
import com.example.pdp_ai.ui.utils.Constants.LESSON
import com.example.pdp_ai.ui.utils.Constants.MODUL
import com.example.pdp_ai.ui.utils.Constants.OPEN_PDP
import com.example.pdp_ai.ui.utils.Constants.PAY
import com.example.pdp_ai.ui.utils.Constants.PAY_HOW
import com.example.pdp_ai.ui.utils.Constants.SER
import com.example.pdp_ai.ui.utils.Constants.STR
import com.example.pdp_ai.ui.utils.Constants.WESUP
import com.example.pdp_ai.ui.utils.Constants.YOUWILL
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {


        val h = "haqida"

        val random = (0..2).random()
        val message = _message.lowercase()

        return when {

            message.contains("salom") -> {
                when (random) {
                    0 -> "Assalomu alaykum"
                    1 -> "Salom"
                    2 -> "Assalomu alaykum va rahmatullohi va barokotuh"
                    else -> ""
                }
            }
            message.contains("bye") -> {
                EXIT
            }
            message.contains("pdp") && message.contains(h) -> {
                STR
            }
            message.contains("open") && message.contains("academy") -> {
                OPEN_PDP
            }

            message.contains("time")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdm = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdm.format(Date(timeStamp.time))

                date.toString()
            }
            message.contains("who") && message.contains("create") -> {
                ABOUT
            }

            message.contains("kurs") && message.contains(h) && message.contains("android") -> {

                "Android haqida"
            }
            message.contains("kurs") && message.contains(h) && message.contains("flutter") -> {

                "flutter haqida"
            }
            message.contains("kurs") && message.contains(h) && message.contains("java") -> {

                "java haqida"
            }
            message.contains("kurs") && message.contains(h) && message.contains("front") -> {

                "frontend haqida"
            }
            message.contains("kurslar") && message.contains(h) -> {

                "Barcha kurslar haqida"
            }

            message.contains("unicorn") -> {

                "Unicorn haqida ma'lumot"
            }

            message.contains("qan") && message.contains("notebook") -> {

                "yaxshi notebook"
            }

            message.contains("kurs") && message.contains("tugat") -> {

                "Har bir modul uchun maksimum 45 kun"
            }

            message.contains("kurs") && message.contains("keyin") && message.contains("foyda") && message.contains(
                "qancha"
            ) -> {
                KURS
            }

            message.contains("to'lov") && message.contains("qanday") && message.contains("amal") -> {
                PAY
            }
            (message.contains("bepul") || message.contains("ochiq")) && (message.contains("kurs") || message.contains(
                "dars"
            )) -> {
                MODUL
            }
            message.contains("sertifikat") && message.contains("beril") && message.contains("bitir") -> {
                SER
            }
            message.contains("kurs") && message.contains("tugat") && message.contains("ish") -> {
                KURSTU
            }
            message.contains("kurs") && message.contains("qaysi") && message.contains("vaqt") && message.contains(
                "foyda"
            ) -> {

                "Har bir modul uchun maksimum 45 kun"
            }

            //In English
            message.contains("hi") || message.contains("hello") -> {
                when (random) {
                    0 -> "Hello"
                    else -> "Hi"
                }
            }
            message.contains("pdp") && message.contains("about") -> {
                STR
            }
            message.contains("thank") -> {
                "You are welcome"
            }
            message.contains("do you speak") || message.contains("can you speak") -> {
                when (random) {
                    0 -> "Yes I can"
                    1 -> "I can speak seven languages"
                    2 -> "Yes of course! And you?"
                    else -> "error"
                }
            }

            message.contains("how are") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            message.contains("android") -> {

                "Info about Android"
            }
            message.contains("flutter") -> {

                "Info about flutter"
            }
            message.contains("java") -> {

                "Info about java"
            }
            message.contains("front") -> {

                "Info about frontend"
            }
            message.contains("courses") && message.contains("about") -> {

                "About all courses"
            }

            message.contains("unicorn") -> {

                "Unicorn haqida ma'lumot"
            }

            (message.contains("how") || message.contains("what")) && message.contains("notebook") -> {

                "good notebook"
            }

            message.contains("course") && message.contains("finish") -> {

                "Each course lasts 45 days"
            }

            message.contains("course") && message.contains("after") -> {
                YOUWILL
            }

            message.contains("pay") && message.contains("how") -> {
                PAY_HOW
            }
            (message.contains("free") || message.contains("open")) && (message.contains("course") || message.contains(
                "dars"
            )) -> {
                LESSON
            }
            message.contains("certificate") && message.contains("give") && message.contains("finish") -> {
                CER
            }
            message.contains("course") && message.contains("finish") && message.contains("job") -> {
                WESUP
            }
            message.contains("*") || message.contains("-") || message.contains("+") || message.contains("/")-> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Bilmayman"
                    2 -> "I don't now.."
                    3 -> "..."
                    else -> "error"
                }
            }
        }
    }
}