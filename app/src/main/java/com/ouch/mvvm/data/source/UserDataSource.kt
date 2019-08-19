package com.ouch.mvvm.data.source

import com.ouch.mvvm.data.BaseUserDataSource
import com.ouch.mvvm.data.model.Result
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by 李小璐 on 2019-08-19.
 */
interface UserService {

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("user/signIn")
    fun singIn(@Body body: RequestBody): Single<Result>
}

@Singleton
class UserDataSource @Inject constructor(@Named("user") val user: UserService): BaseUserDataSource {

    override fun signIn(body: RequestBody): Single<Result> {
        return user.singIn(body)
    }

}