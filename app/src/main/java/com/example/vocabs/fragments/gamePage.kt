package com.example.vocabs.fragments

import androidx.fragment.app.viewModels
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.vocabs.CloudAnimator
import com.example.vocabs.R
import com.example.vocabs.ScoreViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale
import kotlin.properties.Delegates


class gamePage : Fragment() {

    private lateinit var cloud1: ImageView
    private lateinit var cloud2: ImageView
    private lateinit var back_btn: ImageView
    private lateinit var hint: ImageView
    private lateinit var img_11: ImageView
    private lateinit var navController: NavController
    private val handler = Handler()

    private lateinit var l1: TextView
    private lateinit var l2: TextView
    private lateinit var l3: TextView
    private lateinit var l4: TextView
    private lateinit var l5: TextView
    private lateinit var hs: TextView
    private lateinit var cs: TextView
    private lateinit var hintLeft: TextView
    private lateinit var letters: List<TextView>

    private lateinit var img_1: ImageView
    private lateinit var img_2: ImageView
    private lateinit var img_3: ImageView
    private lateinit var img_4: ImageView
    private lateinit var img_5: ImageView
    private lateinit var img_6: ImageView
    private lateinit var img_7: ImageView
    private lateinit var img_8: ImageView
    private lateinit var img_9: ImageView
    private lateinit var img_10: ImageView
    private lateinit var img: List<ImageView>

    private var green by Delegates.notNull<Int>()
    private lateinit var vibrationManager: Vibrator

    private lateinit var hintSound: MediaPlayer
    private lateinit var successSound: MediaPlayer
    private lateinit var woodPileSound: MediaPlayer
    private lateinit var boneBreakSound: MediaPlayer


    private val scoreViewModel: ScoreViewModel by viewModels()

    private var count: Int = -1
    lateinit var word: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_page, container, false)
        vibrationManager = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        context?.let {
            hintSound = MediaPlayer.create(it, R.raw.click_btn)
            successSound = MediaPlayer.create(it, R.raw.success)
            woodPileSound = MediaPlayer.create(it, R.raw.wood_pile)
            boneBreakSound = MediaPlayer.create(it, R.raw.bone_break)
        }

        cloud1 = view.findViewById(R.id.cloud1)
        cloud2 = view.findViewById(R.id.cloud2)
        hint = view.findViewById(R.id.hint)
        hintLeft = view.findViewById(R.id.hint_count)
        back_btn = view.findViewById(R.id.back_btn)

        img_1 = view.findViewById(R.id.img_1)
        img_2 = view.findViewById(R.id.img_2)
        img_3 = view.findViewById(R.id.img_3)
        img_4 = view.findViewById(R.id.img_4)
        img_5 = view.findViewById(R.id.img_5)
        img_6 = view.findViewById(R.id.img_6)
        img_7 = view.findViewById(R.id.img_7)
        img_8 = view.findViewById(R.id.img_8)
        img_9 = view.findViewById(R.id.img_9)
        img_10 = view.findViewById(R.id.img_10)
        img_11 = view.findViewById(R.id.hangedman)

        l1 = view.findViewById(R.id.l1)
        l2 = view.findViewById(R.id.l2)
        l3 = view.findViewById(R.id.l3)
        l4 = view.findViewById(R.id.l4)
        l5 = view.findViewById(R.id.l5)

        cs = view.findViewById(R.id.currScore)
        hs = view.findViewById(R.id.highScore)

        green = context?.resources?.getColor(R.color.green)!!

        img = listOf(img_1, img_2, img_3, img_4, img_5, img_6, img_7, img_8, img_9, img_10, img_11)
        letters = listOf(l1, l2, l3, l4, l5)

        val wordlist = mutableListOf<String>()
        val cloudAnimator = CloudAnimator(requireActivity())
        cloudAnimator.animateClouds(cloud1, cloud2)

        loadWordlist(wordlist)

        initializeGame(wordlist)

        for (keyChar in 'A'..'Z') {
            val keyId = resources.getIdentifier("key_$keyChar", "id", context?.packageName)
            val key = view.findViewById<TextView>(keyId)
            if (key != null) {
                key.setOnClickListener { it ->
                    if(word.contains(keyChar)) {
                        revealLetters(keyChar, it)
                        if (!letters.any {  it.alpha != 1.0f }) {
                            scoreViewModel.setGameWon(true)
                            scoreViewModel.updateScore((scoreViewModel.score.value ?: 0)+ 5)
                        }
                    } else {
                        updateVisibility(it)

                    }
                    it.isClickable = false
                }
            } else {
                Log.e("GamePage", "TextView with id key_$keyChar not found")
            }
        }

        back_btn.setOnClickListener {
            navController.navigate(R.id.action_gamePage_to_startUp)
        }
        var hintCount:Int = 0
        hint.setOnClickListener {
            hintSound.start()
            hintCount++
            handler.postDelayed({
                scoreViewModel.setHintLeft(2 - hintCount)
                if(hintCount == 2){
                    hint.isClickable = false

                }
                var l = letters.random()
                while (l.alpha != 0f) {
                    l = letters.random()
                }
                l.alpha = 1.0f
                checkForWin()
            }, 900)


        }

        return view
    }


    private fun checkForWin() {
        if(!letters.any { it.alpha != 1.0f}) {
            scoreViewModel.setGameWon(true)
            scoreViewModel.updateScore((scoreViewModel.score.value ?: 0) + 5)
            scoreViewModel.setResultScore((scoreViewModel.score.value ?: 0))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        scoreViewModel.hintLeft.observe(viewLifecycleOwner) { hintLeft ->
            this.hintLeft.text = hintLeft.toString()
        }

        scoreViewModel.score.observe(viewLifecycleOwner) { currentScore ->
            cs.text = currentScore.toString()
        }
        scoreViewModel.bestScore.observe(viewLifecycleOwner) { highScore ->
            hs.text = highScore.toString()
        }
        scoreViewModel.isGameWon.observe(viewLifecycleOwner) { isWon ->
            if(isWon) {
                scoreViewModel.setGameWon(false)
                handler.postDelayed(
                    {
                        navController.navigate(R.id.action_gamePage_self)
                    }, 500)
            }
        }
    }

    private fun loadWordlist(wordlist: MutableList<String> ) {
        try {
            val inputStream = context?.assets?.open("wordlist.txt")

            if (inputStream != null) {
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                do {
                    line = reader.readLine()
                    if (line != null) {
                        wordlist.add(line)
                    }
                } while (line != null)
                reader.close()
            } else {
                Log.e("Wordlist", "Input stream is null")
            }
        } catch (e: Exception) {
            Log.e("Wordlist", "Error reading list: $e")
        }
    }

    private fun initializeGame(wordlist: MutableList<String>) {
        if (wordlist.isNotEmpty()) {
            word = wordlist.random().uppercase(Locale.ROOT)
            scoreViewModel.setWord(word)

            if (word.length >= letters.size) {
                letters.forEachIndexed { index, textView ->
                    textView.alpha = 0f
                    textView.text = word[index].toString()
                }
            } else {
                Log.e("Wordlist", "Selected word is too short")
            }
        } else {
            Log.e("Wordlist", "Wordlist is empty")
        }
    }

    private fun updateVisibility(key: View) {

        count++
        val idx = count%img.size
        if(key is TextView) {
            woodPileSound.start()
            handler.postDelayed({
                key.setTextColor(Color.RED)
                if(vibrationManager.hasVibrator() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrationManager.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                }
                if(count == 9) {
                    img[idx].alpha = 1.0f
                    key.postDelayed({
                        for (i in 3..9) {
                            img[i].alpha = 0f
                        }
                    }, 1000)

                    boneBreakSound.start()
                    handler.postDelayed({ img[idx+1].alpha = 1.0f },1000)

                    handler.postDelayed(
                        {
                            navController.navigate(R.id.action_gamePage_to_result)
                            scoreViewModel.resetScore()
                        }, 2000)
                }else {
                    img[idx].alpha = 1.0f
                }
            },500)
        }


    }

    private fun revealLetters(char: Char, key: View) {
        successSound.start()
        handler.postDelayed({
            if(key is TextView) {
                key.setTextColor(green)
            }
            letters.filter { it.text?.contains(char) == true }.forEach {
                it.alpha = 1.0f
            }
            checkForWin()
        }, 700)

    }

}