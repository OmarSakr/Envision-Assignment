package com.codevalley.envisionandroidassignment.view.fragments.library

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codevalley.envisionandroidassignment.R
import com.codevalley.envisionandroidassignment.databinding.FragmentLibraryBinding
import com.codevalley.envisionandroidassignment.utils.AppController
import com.codevalley.envisionandroidassignment.utils.BaseFragment
import com.codevalley.envisionandroidassignment.view.adapters.LibraryAdapter
import com.codevalley.envisionandroidassignment.viewModel.library.LibraryViewModel
import com.codevalley.envisionandroidassignment.viewModel.library.LibraryViewModelFactory
import kotlinx.coroutines.flow.collect


/**
 * LibraryFragment shows all saved paragraphs to use
 */
class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {
    private lateinit var navController: NavController
    private lateinit var libraryAdapter: LibraryAdapter
    private lateinit var libraryViewModel: LibraryViewModel
    override fun getViewBinding() = FragmentLibraryBinding.inflate(layoutInflater)
    override fun setUpViews() {
        navController = Navigation.findNavController(requireActivity(), R.id.fragment)
        initUi()
        initEventDriven()
    }

    private fun initEventDriven() {
        // navigate to captureFragment
        binding.relativeCapture.setOnClickListener {
            navController.navigate(R.id.action_libraryFragment_to_captureFragment)
        }
    }

    private fun initUi() {
        libraryViewModel = ViewModelProvider(
            this,
            LibraryViewModelFactory((requireActivity().application as AppController).database.libraryDao())
        ).get(LibraryViewModel::class.java)
        getLibrary()
        initRecycler()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getLibrary() {
        lifecycleScope.launchWhenStarted {
            libraryViewModel.getLibrary.collect {
                if (it.isNotEmpty()) {
                    libraryAdapter.addAll(it)
                    libraryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initRecycler() {
        libraryAdapter = LibraryAdapter(requireActivity(),navController)
        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding.rvLibrary.layoutManager = linearLayoutManager
        binding.rvLibrary.adapter = libraryAdapter
    }


}