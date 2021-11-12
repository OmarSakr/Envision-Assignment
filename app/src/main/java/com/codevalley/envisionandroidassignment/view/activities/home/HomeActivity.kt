package com.codevalley.envisionandroidassignment.view.activities.home

import com.codevalley.envisionandroidassignment.utils.BaseActivity
import com.codevalley.envisionandroidassignment.databinding.ActivityHomeBinding

/**
 * HomeActivity acts out as a base for fragments
 */
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun setUpViews() {
        val navHostFragment = binding.fragment
    }

    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)


}