package com.zello.mediasessionsample.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zello.mediasessionsample.headset.Model
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainViewModel : ViewModel() {

    private val disposable = CompositeDisposable()
    private val _lastMediaEvent = MutableLiveData<String>()

    val lastMediaEvent = _lastMediaEvent

    init {
        Model.lastMediaEvent.skip(1).observeOn(AndroidSchedulers.mainThread()).subscribe { _lastMediaEvent.value = it }.also { disposable.add(it) }
    }

    override fun onCleared() {
        disposable.dispose()
    }

}
