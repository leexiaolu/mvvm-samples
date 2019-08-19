package com.ouch.mvvm.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by 李小璐 on 2019-08-19.
 */
open class BaseViewModel: ViewModel() {

    val disposables = CompositeDisposable()

    /**
     * ViewModel销毁同时也取消请求
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}