/*
 * Copyright 2020 Abdul-Mujeeb Aliu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mnsons.offlinebank.utils.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import androidx.annotation.ColorRes
import androidx.core.view.children

internal val View.inflater: LayoutInflater get() = LayoutInflater.from(context)


fun View.hide() {
    this.animate().translationY(-this.bottom.toFloat())
        .setInterpolator(AccelerateInterpolator())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.GONE
            }
        })
        .start()
}

fun View.show() {
    this.animate().translationY(0F)
        .setInterpolator(DecelerateInterpolator())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                visibility = View.VISIBLE
            }
        })
        .start()
}


// slide the view from below itself to the current position
fun View.slideUp() {
    this.visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        this.height.toFloat(), // fromYDelta
        0f
    )                // toYDelta
    animate.duration = 500
    this.startAnimation(animate)
}

// slide the view from its current position to below itself
fun View.slideDown() {
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        0f, // fromYDelta
        this.height.toFloat()
    ) // toYDelta
    animate.duration = 500
    this.startAnimation(animate)
    this.visibility = View.GONE
}

fun ViewGroup.recursivelyApplyToChildren(function: (child: View) -> Unit) {
    this.children.forEach {
        function.invoke(it)
        if (it is ViewGroup) {
            it.recursivelyApplyToChildren(function)
        }
    }
}

fun Any.animateBetweenColors(start: Int, end: Int, function: (animatedValue: Int) -> Unit) {
    val animator = ValueAnimator.ofObject(
        ArgbEvaluator(),
        start,
        end
    )
    animator.duration = 500 // milliseconds
    animator.addUpdateListener { valueAnimator ->
        function.invoke(valueAnimator.animatedValue as @kotlin.ParameterName(name = "animatedValue") Int)
    }
    animator.start()
}

fun Activity.animateStatusBarColorChangeTo(@ColorRes endColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        animateBetweenColors(window.statusBarColor, Color.parseColor(getColorHexString(endColor))) {
            window.statusBarColor = it
        }
    }
}

fun checkIfIsMarshMallow(): Boolean {
    // Check if we're running on Android 6.0 or higher
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun slightDelay(function: () -> Unit, timeOut:Long = 1000) {
    val handler = Handler()
    handler.postDelayed({
        function.invoke()
    }, timeOut)
}
