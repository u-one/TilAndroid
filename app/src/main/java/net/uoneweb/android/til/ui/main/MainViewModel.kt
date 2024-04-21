package net.uoneweb.android.til.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : ViewModel() {
        private val _message =
            MutableLiveData<MainFragment.Message>(
                MainFragment.Message("Android", "MainFragment"),
            )
        val message: LiveData<MainFragment.Message> = _message
    }
