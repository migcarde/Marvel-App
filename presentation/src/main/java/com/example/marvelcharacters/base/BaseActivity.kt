package com.example.marvelcharacters.base

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<State: Parcelable, Transition> : AppCompatActivity() {

    companion object {
        const val STATE = "state"
    }

    // Values
    protected abstract val viewModel: BaseViewModel<State, Transition>?

    // Variables
    lateinit var binding: ViewBinding

    // Override functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        setContentView(binding.root)
        savedInstanceState?.getParcelable<State>(STATE)?.apply { viewModel?.loadState(this) }

        initViews()
        initObservers()
        initListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel?.getState()?.value?.apply { outState.putParcelable(STATE, this) }
    }

    // Abstract functions
    abstract fun getViewBinding(): ViewBinding

    // Private functions
    private fun initObservers() {
        viewModel?.getState()?.observe(this, ::manageState)
        viewModel?.getTransition()?.observe(this, ::manageTransition)
    }

    // Protected functions
    protected open fun initViews() {} // Callback

    protected open fun manageState(state: State) {} // Callback

    protected open fun manageTransition(transition: Transition) {} // Callback

    protected open fun initListeners() {} // Callback
}