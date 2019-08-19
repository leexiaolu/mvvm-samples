package com.ouch.mvvm.base

import android.annotation.SuppressLint
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by 李小璐 on 2019-08-19.
 */
@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {

    var disposables = CompositeDisposable()

    fun <T : ViewDataBinding> bind(@LayoutRes layoutId: Int, callback: ((T) -> Unit)? = null): T {
        return DataBindingUtil.setContentView<T>(this, layoutId).also {
            callback?.invoke(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}