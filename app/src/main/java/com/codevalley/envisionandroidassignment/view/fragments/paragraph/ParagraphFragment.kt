package com.codevalley.envisionandroidassignment.view.fragments.paragraph

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.codevalley.envisionandroidassignment.R
import com.codevalley.envisionandroidassignment.databinding.FragmentParagraphBinding
import com.codevalley.envisionandroidassignment.utils.BaseFragment


// ParagraphFragment is used to show the saved paragraphs come from OCR.
class ParagraphFragment : BaseFragment<FragmentParagraphBinding>() {
    private lateinit var navController: NavController
    override fun getViewBinding() = FragmentParagraphBinding.inflate(layoutInflater)

    override fun setUpViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment)
        initUi()
        initEventDriven()
    }

    private fun initEventDriven() {
        binding.ivBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun initUi() {
        //getting saved paragraph from library
        if (arguments?.getString("paragraph") != null) {
            binding.tvParagraph.text = arguments?.getString("paragraph")
        }
    }
}