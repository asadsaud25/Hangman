package com.example.vocabs

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val scoreViewModel: ScoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}

class CloudAnimator(context: Context) {
    private  val screenWidth by lazy { context.resources.displayMetrics.widthPixels }

    fun animateClouds(cloud1: ImageView, cloud2: ImageView) {
        val halfScreenWidth = screenWidth.toFloat() / 2f // Calculate half screen width
        // Cloud 1 Animator
        val cloud1Animator = ObjectAnimator.ofFloat(cloud1, "translationX", -halfScreenWidth, halfScreenWidth).apply {
            duration = 25000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        // Cloud 2 Animator (assuming no reversal needed)
        val cloud2Animator = ObjectAnimator.ofFloat(cloud2, "translationX", halfScreenWidth, -halfScreenWidth).apply {
            duration = 30000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        cloud1Animator.start()
        cloud2Animator.start()
    }




}