package com.mazzone.isere.transport.ui.fragment.stop;

import com.crashlytics.android.Crashlytics;
import com.mazzone.isere.transport.interactor.TransInfoInteractor;
import com.mazzone.isere.transport.ui.model.HourItem;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StopPresenterImpl implements StopPresenter {

    //View
    private StopFragmentView mView;

    //Interactor
    @Inject TransInfoInteractor mTransInfoInteractor;

    private String mStopId;
    private String mName;
    private String city;

    @Inject
    public StopPresenterImpl(StopFragmentView view) {
        mView = view;
    }

    @Override
    public void bindView(StopFragmentView view) {
        mView = view;
        if (mStopId != null) // Update content if not null
            updateStop(mStopId, mName, city);
    }

    // TODO: 29/10/2016 unbind view

    @Override
    public void updateStop(String stopId, String name, String city) {
        //Update var
        mStopId = stopId;
        mName = name;
        this.city = city;

        //Update view
        mView.showName(name);
        mView.showCity(city);

        //Make update call
        mTransInfoInteractor
                .getHourItemByLogicalId(stopId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<HourItem>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mView.showLoading(true);
                        mView.showHours(null);
                        mView.showEmpty(false);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        mView.showEmpty(true);
                        mView.showMessage(e.getLocalizedMessage());
                        Crashlytics.logException(e);
                    }

                    @Override
                    public void onNext(List<HourItem> hourItems) {
                        mView.showLoading(false);
                        mView.showEmpty(hourItems == null || hourItems.isEmpty());
                        mView.showHours(hourItems);
                    }
                });
    }
}
