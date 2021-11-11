package com.codevalley.envisionandroidassignment.view.activities.splash

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.codevalley.envisionandroidassignment.utils.BaseActivity
import com.codevalley.envisionandroidassignment.databinding.ActivitySplashBinding
import com.codevalley.envisionandroidassignment.view.activities.home.HomeActivity
import com.codevalley.envisionandroidassignment.viewModel.splashViewModel.SplashViewModel
import kotlinx.coroutines.flow.collect


class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private lateinit var splashViewModel: SplashViewModel

    override fun setUpViews() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        splashViewModel.startTimer()
        launchWhenTimerFinished()
    }


    private fun launchWhenTimerFinished() {
        lifecycleScope.launchWhenStarted {
            splashViewModel.mutableStateFlow.collect {
                if (it == 0) {
                    val intent =
                        Intent(this@SplashActivity, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    startActivity(intent)
                }
            }
        }
    }

    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)
}
