package com.zello.mediasessionsample.headset

import android.util.Log
import android.view.KeyEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject

object Model {

    private val _lastMediaEvent: Subject<String> = BehaviorSubject.createDefault("")

    val lastMediaEvent: Observable<String> = _lastMediaEvent

    fun onMediaKeyEvent(event: KeyEvent) {
        Log.i("MEDIA SESSION SAMPLE", "Media key $event")
        _lastMediaEvent.onNext(event.toString())
    }

}
