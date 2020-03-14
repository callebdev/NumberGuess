package com.calleb.numberguess

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.floor

class MainActivity : AppCompatActivity() {

    private var started = false
    private var number = 0
    private var tries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchSavedInstanceData(savedInstanceState)

        btnDoGuess.isEnabled = started
        // btnDoGuess.visibility = View.GONE
    }

    // When the Button Start is clicked
    fun start(v: View) {
        log("Game started!")
        edtNum.setText("")
        started = true
        btnDoGuess.isEnabled = started
        txtStatus.text = getString(R.string.guess_hint, 1, 7)
        number = 1 + floor(Math.random() * 7).toInt() // Generates a random number
        tries = 0
    }

    // When the Guess Button is clicked
    fun guess(v: View) {
        if (edtNum.text.toString() == "") return

        tries++
        log("Guessed number ${edtNum.text} in $tries tries")

        val g = edtNum.text.toString().toInt()

        when {
            g < number -> {
                txtStatus.setText(R.string.status_too_low)
                edtNum.setText("")
            }
            g > number -> {
                txtStatus.setText(R.string.status_too_high)
                edtNum.setText("")
            }
            else -> {
                txtStatus.visibility = View.VISIBLE
                txtStatus.text = getString(R.string.status_hit, tries)
                started = false
                btnDoGuess.isEnabled = started
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        putInstanceData(outState)
    }

    private fun putInstanceData(outState: Bundle?) {
        if (outState != null) {
            with(outState) {
                putBoolean("started", started)
                putInt("number", number)
                putInt("tries", tries)
                putString("statusMsg", txtStatus.text.toString())
                putStringArrayList("logs",
                    ArrayList(console.text.split("\n")))
            }
        }
    }

    private fun fetchSavedInstanceData(
        savedInstanceState: Bundle?
    ) {
        if (savedInstanceState != null) {
            with(savedInstanceState) {
                started = getBoolean("started")
                number = getInt("number")
                tries = getInt("tries")
                txtStatus.text = getString("statusMsg")
                console.text = getStringArrayList("logs")!!
                    .joinToString("\n")
            }
        }
    }

    private fun log(msg: String) {
        Log.d("LOG", msg)
        console.log(msg)
    }
}

class Console(ctx: Context, aset: AttributeSet? = null)
    : ScrollView(ctx, aset) {
    val tv = TextView(ctx)
    var text: String
        get() = tv.text.toString()
        set(value) {
            tv.text = value
        }
    init {
        setBackgroundColor(0x40FFFF00)
        addView(tv)
    }
    fun log(msg: String) {
        val l = tv.text.let {
            if (it == "") listOf() else it.split("\n")
        }.takeLast(100) + msg
        tv.text = l.joinToString("\n")
        post { fullScroll(FOCUS_DOWN) }
    }
}
