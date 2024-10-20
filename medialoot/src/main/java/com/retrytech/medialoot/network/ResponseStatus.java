package com.retrytech.medialoot.network;

public interface ResponseStatus {
    void onFail(Object obj);

    void onSuccess(Object obj);
}