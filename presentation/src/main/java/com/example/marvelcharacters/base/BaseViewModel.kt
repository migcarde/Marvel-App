package com.example.marvelcharacters.base

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.commons_android.SingleLiveEvent

abstract class BaseViewModel<State: Parcelable, Transition>: ViewModel() {
    protected val viewState = MutableLiveData<State>()
    protected val viewTransition = SingleLiveEvent<Transition>()

    fun getState() = viewState as LiveData<State>
    fun getTransition() = viewTransition as LiveData<Transition>

    fun loadState(state: State) {
        this.viewState.value = state
    }
}