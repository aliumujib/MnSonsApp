package com.mnsons.offlinebank.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mnsons.offlinebank.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
    }

}