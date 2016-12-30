package com.mazzone.isere.transport.ui.activity;

import com.crashlytics.android.Crashlytics;
import com.mazzone.isere.transport.api.model.StopPoint;
import com.mazzone.isere.transport.interactor.TransInfoInteractor;
import com.mazzone.isere.transport.util.Strings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainPresenterImpl implements MainPresenter {

    private List<StopPoint> mStopSearchResult;

    private MainActivityView mView;

    //Interactor
    @Inject TransInfoInteractor mTransInfoInteractor;

    @Inject
    public MainPresenterImpl(MainActivityView view) {
        mView = view;
    }

    @Override
    public void searchQuery(String query) {
        if(mView!=null)
            mView.presentSearchProgress(true);

        mTransInfoInteractor
                .getResultForQuery(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<StopPoint>, Observable<StopPoint>>() {
                    @Override
                    public Observable<StopPoint> call(List<StopPoint> stopPoints) {
                        return Observable.from(stopPoints);
                    }
                })
                .distinct(new Func1<StopPoint, String>() {
                    @Override
                    public String call(StopPoint stopPoint) {
                        return stopPoint.getLogicalStop().getId();
                    }
                })
                .toList()
                .subscribe(new Observer<List<StopPoint>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                    }

                    @Override
                    public void onNext(List<StopPoint> stopPoints) {
                        if (mView != null) {
                            mStopSearchResult = stopPoints;
                            List<String> results = new ArrayList<>();
                            for (StopPoint stopPoint : stopPoints) {
                                String stopName = String.format("%s - %s",
                                        Strings.capitalizeFirstLetterEachWord(stopPoint.getName()),
                                        Strings.capitalizeFirstLetterEachWord(stopPoint.getLocality().getName()));
                                results.add(stopName);
                            }
                            mView.presentSearchProgress(false);
                            mView.presentSearchResult(results);
                        }
                    }
                });
    }

    @Override
    public void stopSelected() {
        //Hide search
        if (mView != null)
            mView.presentHideSearchFeature();
    }

    @Override
    public void stopSelectionAtPosition(int position) {
        StopPoint selectedStop = mStopSearchResult.get(position);
        if (selectedStop != null)
            if (mView != null)
                mView.presentStopSelection(selectedStop);
    }
}
