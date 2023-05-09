package com.qr.myqr.data.http

import okhttp3.Headers

interface IHttpCallback {

    fun onSuccess(headers: Headers, body: String?)
    fun onError(code: Int, error: String?)

}