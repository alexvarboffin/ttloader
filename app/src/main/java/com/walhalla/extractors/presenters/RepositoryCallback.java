package com.walhalla.extractors.presenters;

import com.walhalla.ttvloader.TTResponse;

public interface RepositoryCallback {
    void successResult(TTResponse result);

    void errorResult(String error);

    void showProgressDialog();

    void hideProgressDialog();

    void errorResult(int errWwwNotSupport);
}
