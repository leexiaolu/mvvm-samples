package com.ouch.mvvm.ext

import androidx.databinding.ObservableField
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject

/**
 * Created by 李小璐 on 2019-08-19.
 */

/**
 * 提供辅助方法帮助监听ObservableField变化
 */
fun <T: androidx.databinding.Observable> T.onChanged(callback: (T) -> Unit) =
    object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: androidx.databinding.Observable?, i: Int) =
            callback(observable as T)
    }.also { addOnPropertyChangedCallback(it) }.let {
        Disposables.fromAction {removeOnPropertyChangedCallback(it)}
    }


/**
 * 把DataBinding的ObservableField转换成RxJava的Observable
 */
fun <T> ObservableField<T>.toObservable(): io.reactivex.Observable<T> {
    val subject = PublishSubject.create<T>()
    this.addOnPropertyChangedCallback(object : androidx.databinding.Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: androidx.databinding.Observable, i: Int) {
            val v = observable as ObservableField<T>
            if (v.get() != null) {
                subject.onNext(v.get()!!)
            }
        }
    })

    return subject
}