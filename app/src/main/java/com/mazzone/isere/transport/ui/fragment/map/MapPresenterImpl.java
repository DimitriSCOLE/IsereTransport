package com.mazzone.isere.transport.ui.fragment.map;

import com.crashlytics.android.Crashlytics;
import com.mazzone.isere.transport.interactor.TransInfoInteractor;
import com.mazzone.isere.transport.ui.model.MapItem;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapPresenterImpl implements MapPresenter {

    //View
    private MapFragmentView mView;
    private MapFragment.OnStopSelectedListener mOnStopSelectedListener;

    //Interactor
    @Inject TransInfoInteractor mTransInfoInteractor;

    //Data
    private HashMap<String, MapItem> mMapItemHashMap = new HashMap<>();

    @Inject
    public MapPresenterImpl(MapFragmentView view) {
        mView = view;
    }

    @Override
    public void boundsUpdate(double northEastLatitude, double northEastLongitude,
                             double southWestLatitude, double southWestLongitude,
                             float zoom) {
        mTransInfoInteractor.getMapItemInBounds(
                northEastLatitude, northEastLongitude,
                southWestLatitude, southWestLongitude,
                zoom)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<MapItem>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (mOnStopSelectedListener != null)
                            mOnStopSelectedListener.showProgress(true);
                    }

                    @Override
                    public void onCompleted() {
                        //Progress
                        if (mOnStopSelectedListener != null)
                            mOnStopSelectedListener.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mOnStopSelectedListener != null)
                            mOnStopSelectedListener.showProgress(false);
                        mView.showMessage(e.getLocalizedMessage());
                        Crashlytics.logException(e);
                    }

                    @Override
                    public void onNext(List<MapItem> mapItems) {
                        //Cleanup
                        mView.clear();
                        mMapItemHashMap.clear();

                        //Add
                        for (MapItem mapItem : mapItems) {
                            mMapItemHashMap.put(mapItem.getId(), mapItem);
                            mView.addMapItem(mapItem);
                        }
                    }
                });


    }

    @Override
    public void selectMapItem(String id) {
        if (mOnStopSelectedListener != null)
            mOnStopSelectedListener.onStopSelected(mMapItemHashMap.get(id).getLogicalStopId(), mMapItemHashMap.get(id).getName(), mMapItemHashMap.get(id).getCity());
    }

    @Override
    public void selectMap() {
        if (mOnStopSelectedListener != null)
            mOnStopSelectedListener.onUnSelected();
    }

    @Override
    public void bindCallback(MapFragment.OnStopSelectedListener listener) {
        mOnStopSelectedListener = listener;
        //Default to no loading
        if (mOnStopSelectedListener != null)
            mOnStopSelectedListener.showProgress(false);
    }

    @Override
    public void unbindCallback() {
        mOnStopSelectedListener = null;
    }

}
