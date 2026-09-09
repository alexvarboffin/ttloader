package com.walhalla.adapters

class EmptyViewModel {
    constructor(error: String) {
        this.error = error
    }
    @JvmField var error: String? = null
}
