package com.example.marvelcharacters.base

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.marvelcharacters.MainActivity

abstract class BaseFragment<State: Parcelable, Transition>: Fragment() {

    companion object {
        const val STATE = "state"
    }

    // Values
    protected abstract val viewModel: BaseViewModel<State, Transition>?

    // Variables
    lateinit var _binding: ViewBinding
    private var isInstanceSaved = false

    // Override functions
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(container)

        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.getParcelable<State>(STATE)?.apply { viewModel?.loadState(this) }
        isInstanceSaved = false

        initViews()
        initObservers()
        initListeners()
    }

    // Private functions
    private fun initObservers() {
        viewModel?.getState()?.observe(viewLifecycleOwner, ::manageState)
        viewModel?.getTransition()?.observe(viewLifecycleOwner, ::manageTransition)
    }

    private fun getMainActivity(): MainActivity? = when(activity) {
        is MainActivity -> activity as MainActivity
        else -> null
    }

    // Abstract functions
    abstract fun getViewBinding(container: ViewGroup?): ViewBinding

    protected abstract fun initViews()

    protected abstract fun initListeners()

    protected abstract fun manageState(state: State)

    protected abstract fun manageTransition(transition: Transition)
}