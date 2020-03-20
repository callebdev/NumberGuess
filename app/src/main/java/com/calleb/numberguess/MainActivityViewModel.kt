package com.calleb.numberguess

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.floor

class MainActivityViewModel : ViewModel() {

    private var generatedNumber = 0
    private var tryCount = 0

    private val _gameStarted = MutableLiveData<Boolean>()
    val gameStarted
        get() = _gameStarted

    private val _gameStatus = MutableLiveData<String>()
    val gameStatus
        get() = _gameStatus

    private val _gameLog = MutableLiveData<String>()
    val gameLog
        get() = _gameLog

    init {
        _gameStarted.value = false
        _gameStatus.value = ""
        _gameLog.value = ""
    }

    private fun generateRandomNumber() = 1 + floor(Math.random() * 7).toInt()

    fun guess(number: Int) {
        tryCount++
        log("Guessed number $number in $tryCount tries")
        when {
            number < generatedNumber -> {
                _gameStatus.value = "Too low"
            }
            number > generatedNumber -> {
                _gameStatus.value = "Too high"
            }
            number == generatedNumber-> {
                _gameStatus.value = "You got it after $tryCount tries! Press START for a new game."
                _gameStarted.value = false
            }
        }
    }

    private fun log(message: String) {
        _gameLog.value = message
    }

    fun startGame(){
        log("Game started!")
        _gameStarted.value = true
        generatedNumber = generateRandomNumber()
        tryCount = 0
        _gameStatus.value = "Guess a number between 1 and 7"
    }
}