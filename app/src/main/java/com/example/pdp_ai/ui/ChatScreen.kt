package com.example.pdp_ai.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.pdp_ai.R
import com.example.pdp_ai.ui.data.MyLifeCycle
import com.example.pdp_ai.ui.data.Message
import com.example.pdp_ai.ui.utils.BotResponse
import com.example.pdp_ai.ui.utils.Constants
import com.example.pdp_ai.ui.utils.Time
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList

class ChatScreen : AppCompatActivity(), TextToSpeech.OnInitListener {
    private val REQ_CODE = 100
    lateinit var textToSpeech: TextToSpeech
    private val uri = "https://maps.app.goo.gl/G5y2oFnRuAxswXRF9"
    private val API_UZ = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/UzbekDB"
    private lateinit var myLifeCycle: MyLifeCycle

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Maya", "Farida", "Mohichehra", "Nargiza")

    private lateinit var uzbekDB: JSONArray
    private lateinit var englishDB: JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        textToSpeech = TextToSpeech(this, this)
        recyclerView()
        clickEvents()

        myLifeCycle = MyLifeCycle(this, lifecycle)

        connectUzbekAPI()
        connectEnglishAPI()

        lifecycle.addObserver(myLifeCycle)

        val random = (0..3).random()
        customBotMessage(
            "Assalomu alaykum, Siz PDP akademiyasiga murojaat qildingiz! " +
                    "Ismim ${botList[random]} , Sizning shaxsiy konsultantingizman. Sizga qanday yordam bera olaman ? ??????"
        )
    }

    private fun connectEnglishAPI() {
        val jsonArrayENGLISH = JSONArray()
        val API_ENGLISH = "https://61b337cdaf5ff70017ca1d4b.mockapi.io/pdp/ai/db/EnglishDB"
        val queueENGLISH = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonArrayRequest (
            Request.Method.GET, API_ENGLISH, jsonArrayENGLISH, {
                setEnglish(it)
            }, {
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_LONG).show()
            }
        )
        queueENGLISH.add(jsonObjectRequest)
    }
    private fun setEnglish(jsonArray: JSONArray) {
        uzbekDB = jsonArray
    }

    private fun connectUzbekAPI() {
        val jsonArrayUZBEK = JSONArray()
        val API_UZBEK = API_UZ
        val queueUZBEK = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest (
            Request.Method.GET, API_UZBEK, jsonArrayUZBEK, {
                setUzbek(it)
            }, {
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_LONG).show()
            }
        )
        queueUZBEK.add(jsonArrayRequest)
    }
    private fun setUzbek(jsonArray: JSONArray) {
        uzbekDB = jsonArray
    }

    private fun clickEvents() {
        btnSend.setOnClickListener {
            sendMessage()
            btnSend.visibility = View.GONE
            btnVoice.visibility = View.VISIBLE
        }
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    recycleView.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
        et_message.addTextChangedListener {
            if (et_message.text.isNotEmpty()){
                btnVoice.visibility = View.GONE
                btnSend.visibility = View.VISIBLE
            } else {
                btnSend.visibility = View.GONE
                btnVoice.visibility = View.VISIBLE
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                recycleView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            messagesList.add(Message(message, Constants.SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, Constants.SEND_ID, timeStamp))
            recycleView.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            online.text = "typing..."
            delay(1800)

            withContext(Dispatchers.Main) {

                val response = BotResponse.basicResponses(message, uzbekDB, "Uzbek")

                messagesList.add(Message(response, Constants.RECEIVE_ID, timeStamp))

                adapter.insertMessage(Message(response, Constants.RECEIVE_ID, timeStamp))

                recycleView.scrollToPosition(adapter.itemCount - 1)
                online.text = "online"
                textToSpeechFun(response)
                when (response) {
                    Constants.OPEN_PDP -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.pdp.uz/")
                        startActivity(site)
                    }
                    Constants.CALL -> {
                        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998787774747")))
                    }
                    Constants.EXIT -> {
                        delay(1700)
                        finish()
                    }
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        GlobalScope.launch {
            delay(1400)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, Constants.RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, Constants.RECEIVE_ID, timeStamp))

                recycleView.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    fun menu(view: View) {
        val factory = LayoutInflater.from(this)
        val dialogView: View = factory.inflate(R.layout.menu_lay, null)
        val dialog = AlertDialog.Builder(this).create()
        dialog.setView(dialogView)
        dialogView.findViewById<TextView>(R.id.location).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            val intentChooser = Intent.createChooser(intent, "Map")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.clear).setOnClickListener {
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.official).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://pdp.uz/")
            val intentChooser = Intent.createChooser(intent, "Launch Chrome")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.info).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse("https://t.me/pdpuz"))
            val intentChooser = Intent.createChooser(intent, "Launch Telegram")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.openYou).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = (Uri.parse("https://www.youtube.com/c/pdpuz"))
            val intentChooser = Intent.createChooser(intent, "Launch Youtube")
            startActivity(intentChooser)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun call(view: View) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+998787774747"))
        startActivity(intent)
    }

    fun voice(view: View) {
        speechFun()
    }

    private fun speechFun() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something..")
        try {
            startActivityForResult(intent, REQ_CODE)
        } catch (exp: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Speech not supported..", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK || null != data) {
                val res: ArrayList<String> =
                    data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                et_message.setText(res[0])
                sendMessage()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val res: Int = textToSpeech.setLanguage(Locale.CANADA)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(
                    applicationContext,
                    "This language is not supported",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                textToSpeechFun("")
            }
        } else {
            Toast.makeText(applicationContext, "Failed to initialize", Toast.LENGTH_SHORT).show()
        }
    }

    private fun textToSpeechFun(str: String) {
        val strText = str
        textToSpeech.speak(strText, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}