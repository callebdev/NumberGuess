package com.calleb.numberguess

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.gameStarted.observe(this, Observer { started ->
            btnDoGuess.isEnabled = started
            edtNum.isEnabled = started
        })

        viewModel.gameStatus.observe(this, Observer { status ->
            txtStatus.text = status
        })

        viewModel.gameLog.observe(this, Observer { message ->
            Log.d("LOG", message)
            console.log(message)
        })
    }

    // When the Button Start is clicked
    fun start(v: View) {
        viewModel.startGame()
    }

    // When the Guess Button is clicked
    fun guess(v: View) {
        if (edtNum.text.toString() == "") return
        val g = edtNum.text.toString().toInt()
        viewModel.guess(g)
        edtNum.setText("")
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
