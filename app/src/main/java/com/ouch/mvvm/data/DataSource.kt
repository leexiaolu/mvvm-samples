package com.ouch.mvvm.data

import com.ouch.mvvm.data.model.Result
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body

/**
 * Created by 李小璐 on 2019-08-19.
 */
interface BaseUserDataSource {

    fun signIn(body: RequestBody): Single<Result>

}