package com.walhalla.extractors.presenters

import com.walhalla.ttvloader.TTResponse

interface RepositoryCallback {
    abstract fun successResult(result: TTResponse)
    abstract fun errorResult(error: String)
    abstract fun showProgressDialog()
    abstract fun hideProgressDialog()
    abstract fun errorResult(errWwwNotSupport: Int)
}
