package com.ouch.mvvm.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by 李小璐 on 2019-08-19.
 */
open class BaseFragment: Fragment() {

    val disposables = CompositeDisposable()

    fun <T: ViewDataBinding> bind(inflater: LayoutInflater, @LayoutRes layoutId: Int, container: ViewGroup?, callback: ((T)-> Unit)? = null): T {
        return DataBindingUtil.inflate<T>(inflater, layoutId, container, false).also {
            callback?.invoke(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}