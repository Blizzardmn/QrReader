package com.qr.myqr.data.http

interface IHttpCallback {

    fun onSuccess(body: String?)
    fun onError(code: Int, error: String?)

}