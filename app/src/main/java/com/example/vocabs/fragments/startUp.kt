package com.example.vocabs.fragments


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.vocabs.CloudAnimator
import com.example.vocabs.R
import com.example.vocabs.ScoreViewModel

class startUp : Fragment() {

    private lateinit var cloud1: ImageView
    private lateinit var cloud2: ImageView
    private lateinit var bestScore: TextView
    private lateinit var navController: NavController
    private lateinit var play: ImageView
    private val scoreViewModel: ScoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start_up, container, false)

        cloud1 = view.findViewById(R.id.cloud1)
        cloud2 = view.findViewById(R.id.cloud2)
        bestScore = view.findViewById(R.id.high_score)
        play = view.findViewById(R.id.play)

        val cloudAnimator = CloudAnimator(requireActivity())
        cloudAnimator.animateClouds(cloud1, cloud2)

        val startSound = MediaPlayer.create(context, R.raw.start)
        play.setOnClickListener {
            startSound.start()
            Handler().postDelayed( {
                navController = Navigation.findNavController(view)
                navController.navigate(R.id.action_startUp_to_gamePage)
                scoreViewModel.resetResultScore()
                scoreViewModel.resetWord()
            }, 500)

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        scoreViewModel.bestScore.observe(viewLifecycleOwner) { highScore ->
            bestScore.text = highScore.toString()
        }
    }

}