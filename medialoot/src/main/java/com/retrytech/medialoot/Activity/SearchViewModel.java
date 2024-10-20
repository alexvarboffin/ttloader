package com.retrytech.medialoot.Activity;

import android.content.Context;

import com.retrytech.medialoot.network.ApiService;
import com.retrytech.medialoot.MyApplication;

import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SearchViewModel extends Observable {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mJobsDetails;
    Context context;

    public SearchViewModel(Context context) {
        this.context = context;
    }

    public void getAutoSearch(String query,String client,String language) {
        MyApplication application = MyApplication.create(context);
        ApiService apiService = application.getApiService();

        Disposable disposable = apiService.getGoogleSuggetion(query,client,language)
                .subscribeOn(application.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String jobsDetails) throws Exception {

                        jobDataSetChange(jobsDetails);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void jobDataSetChange(String jobsDetails) {
        this.mJobsDetails = jobsDetails;
        setChanged();
        notifyObservers();
    }

    public String getmJobsDetails() {
        return mJobsDetails;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}
