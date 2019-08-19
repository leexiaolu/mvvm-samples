package com.ouch.mvvm.helper

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * Created by 李小璐 on 2019-08-19.
 */
class RxBus {

    private val disposableMap = mutableMapOf<Callback, Disposable>()
    private val _bus = PublishSubject.create<Any>().toSerialized()

    companion object {
        val instance = RxBus()
    }

    fun send(event: RxEvent) {
        _bus.onNext(event)
    }

    fun register(callback: Callback) {
        val consumer = Consumer<Any> {
            callback.let { callback ->
                if (it is RxEvent) {
                    callback.callback(it)
                }
            }
        }
        val disposable = _bus.observeOn(AndroidSchedulers.mainThread()).subscribe(consumer)
        disposableMap[callback] = disposable
    }

    fun unregister(callback: Callback) {
        disposableMap.let {
            if (it.containsKey(callback)) {
                if (!disposableMap[callback]!!.isDisposed) {
                    disposableMap[callback]!!.dispose()
                }
                disposableMap.remove(callback)
            }
        }
    }

    interface Callback {
        fun callback(event: RxEvent)
    }
}

class RxEvent {
    companion object {
        const val EVENT_SAMPLE = 101
    }

    val code: Int
    var any: Any? = null
    
    constructor(code: Int) {
        this.code = code
    }

    constructor(code: Int, any: Any?) {
        this.code = code
        this.any = any
    }
}