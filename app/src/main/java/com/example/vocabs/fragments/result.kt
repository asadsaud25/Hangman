package com.example.vocabs.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.vocabs.R
import com.example.vocabs.ScoreViewModel

class result : Fragment() {

    private lateinit var navController: NavController
    private lateinit var word: String

    private val scoreViewModel : ScoreViewModel by viewModels()

    private lateinit var score: TextView
    private lateinit var l1: TextView
    private lateinit var l2: TextView
    private lateinit var l3: TextView
    private lateinit var l4: TextView
    private lateinit var l5: TextView
    private lateinit var letters: List<TextView>

    private lateinit var home: ImageView
    private lateinit var playAgain: Button

    private lateinit var clickSound: MediaPlayer



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        l1 = view.findViewById(R.id.l1)
        l2 = view.findViewById(R.id.l2)
        l3 = view.findViewById(R.id.l3)
        l4 = view.findViewById(R.id.l4)
        l5 = view.findViewById(R.id.l5)

        score =  view.findViewById(R.id.resultScore)
        playAgain = view.findViewById(R.id.play_again)
        home = view.findViewById(R.id.home)
        letters = listOf(l1, l2, l3, l4, l5)

        context.let {
            clickSound = MediaPlayer.create(context, R.raw.click_btn)
        }


        playAgain.setOnClickListener {
            clickSound.start()
            Handler().postDelayed({
                navController.navigate(R.id.action_result_to_gamePage)
                scoreViewModel.resetResultScore()
            }, 500)
        }

        home.setOnClickListener {
            clickSound.start()
            Handler().postDelayed({
                navController.navigate(R.id.action_result_to_startUp)
            },500)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        scoreViewModel.resultScore.observe(viewLifecycleOwner) { resultScore ->
            score.text = resultScore.toString()
        }
        scoreViewModel.word.observe(viewLifecycleOwner) { correctWord ->
            word = correctWord
            letters.forEachIndexed { index, textView ->
                textView.text = word[index].toString()
            }
        }

    }

}