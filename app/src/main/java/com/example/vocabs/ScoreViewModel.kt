package com.example.vocabs

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ScoreViewModel(application: Application) : AndroidViewModel(application) {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _bestScore = MutableLiveData<Int>()
    val bestScore: LiveData<Int>
        get() = _bestScore

    private val _isGameWon = MutableLiveData<Boolean>(false)
    val isGameWon: LiveData<Boolean>
        get() = _isGameWon

    private val _hintLeft = MutableLiveData<Int>(2)
    val hintLeft: LiveData<Int>
        get() = _hintLeft

    private val _resultScore = MutableLiveData<Int>(0)
    val resultScore: LiveData<Int>
        get() = _resultScore

    private val _word = MutableLiveData<String>("")
    val word: LiveData<String>
        get() = _word

    private val sharedPreferences = getApplication<Application>().getSharedPreferences("vocabprefs", Context.MODE_PRIVATE)

    init {
        _bestScore.value = sharedPreferences.getInt("bestScore", 0)
        _score.value = sharedPreferences.getInt("currScore", 0)
        _resultScore.value = sharedPreferences.getInt("resultScore", 0)
        _word.value = sharedPreferences.getString("correctWord", "ABCDE")
    }

    fun updateScore(newScore: Int) {
        _score.value = newScore
        saveCurrScore(newScore)
        if(newScore > (_bestScore.value ?: 0)) {
            _bestScore.value = newScore
            with(sharedPreferences.edit()) {
                putInt("bestScore", newScore)
                apply()
            }
        }
    }

    fun resetScore() {
        _score.value = 0
        saveCurrScore(0)
    }

    private fun saveCurrScore(score: Int){
        with(sharedPreferences.edit()) {
            putInt("currScore" , score)
            apply()
        }
    }

    fun setGameWon(isWon: Boolean) {
        _isGameWon.value = isWon
    }

    fun setHintLeft(hintLeft: Int) {
        _hintLeft.value = hintLeft
    }

    fun setResultScore(resultScore: Int) {
        _resultScore.value = resultScore
        saveResult(resultScore)
    }

    private fun saveResult(resultScore: Int){
        with(sharedPreferences.edit()) {
            putInt("resultScore", resultScore)
            apply()
        }
    }

    fun resetResultScore() {
        _resultScore.value = 0
        saveResult(0)
    }

    fun setWord(word: String) {
        _word.value = word
        saveCorrectWord(word)
    }
    private fun saveCorrectWord(correctWord: String) {
        with(sharedPreferences.edit()) {
            putString("correctWord", correctWord)
            apply()
        }
    }
    fun resetWord() {
        _word.value = ""
    }

}