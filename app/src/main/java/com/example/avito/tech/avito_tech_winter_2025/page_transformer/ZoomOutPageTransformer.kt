package com.example.avito.tech.avito_tech_winter_2025.page_transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    companion object {
        private const val MIN_SCALE = 0.9f
        private const val MIN_ALPHA = 0.7f
    }

    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = MIN_ALPHA
                }

                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well.
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))

                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }

                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = MIN_ALPHA
                }
            }
        }
    }
}