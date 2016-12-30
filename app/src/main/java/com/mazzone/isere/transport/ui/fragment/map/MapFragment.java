package com.mazzone.isere.transport.ui.fragment.map;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mazzone.isere.transport.TransportApplication;
import com.mazzone.isere.transport.interactor.TransInfoInteractorImpl;
import com.mazzone.isere.transport.ui.model.MapItem;
import com.mazzone.isere.transport.util.BitmapUtil;
import com.mazzone.isere.transport.util.MapItemUtil;
import com.sdoward.rxgooglemap.MapObservableProvider;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends SupportMapFragment implements MapFragmentView, OnMapReadyCallback {

    private static final int MAP_MOVE_DEBOUNCE_TIME_MS = 500;
    private static final int MARKER_ANIMATION_TIME_MS = 300;

    private static final int REQUEST_LOCATION = 1;

    private OnStopSelectedListener mCallback;
    private CompositeSubscription mapCompositeSubscription = new CompositeSubscription();
    private MapObservableProvider mapObservableProvider;

    public interface OnStopSelectedListener {
        void onStopSelected(String stopPointId, String name, String city);

        void onUnSelected();

        void showProgress(boolean isVisible);
    }

    private GoogleMap mGoogleMap;

    @Inject MapPresenter mapPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inject Dagger
        TransportApplication
                .get(getActivity())
                .getAppComponent()
                .plus(new MapFragmentModule(this))
                .inject(this);

        //Retain instance
        setRetainInstance(true);
        //Request map
        getMapAsync(this);
        //Map observable provider
        mapObservableProvider = new MapObservableProvider(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Attach Callback
        try {
            mCallback = (OnStopSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnStopSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapPresenter.bindCallback(mCallback);
        //Observe camera change
        mapCompositeSubscription.add(
                mapObservableProvider
                        .getCameraChangeObservable()
                        .debounce(MAP_MOVE_DEBOUNCE_TIME_MS, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showMessage(throwable.getLocalizedMessage());
                                Crashlytics.logException(throwable);
                            }
                        })
                        .subscribe(new Action1<CameraPosition>() {
                            @Override
                            public void call(CameraPosition cameraPosition) {
                                LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
                                mapPresenter.boundsUpdate(
                                        bounds.northeast.latitude,
                                        bounds.northeast.longitude,
                                        bounds.southwest.latitude,
                                        bounds.southwest.longitude,
                                        cameraPosition.zoom
                                );
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showMessage(throwable.getLocalizedMessage());
                            }
                        }));
        //Observe map click
        mapCompositeSubscription.add(
                mapObservableProvider
                        .getMapClickObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showMessage(throwable.getLocalizedMessage());
                                Crashlytics.logException(throwable);
                            }
                        })
                        .subscribe(new Action1<LatLng>() {
                            @Override
                            public void call(LatLng latLng) {
                                mapPresenter.selectMap();
                            }
                        }));
        //Observe marker click
        mapCompositeSubscription.add(
                mapObservableProvider
                        .getMarkerClickObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                showMessage(throwable.getLocalizedMessage());
                                Crashlytics.logException(throwable);
                            }
                        })
                        .subscribe(new Action1<Marker>() {
                            @Override
                            public void call(Marker marker) {
                                String key = (String) marker.getTag();
                                if (key != null) {
                                    //Select Item
                                    mapPresenter.selectMapItem(key);
                                    //Animate
                                    animateMapTo(marker.getPosition());
                                }
                            }
                        }));
    }

    @Override
    public void onStop() {
        super.onStop();
        mapPresenter.unbindCallback();
        //Clear subscription
        mapCompositeSubscription.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Set map
        mGoogleMap = googleMap;
        //Set settings
        googleMap.setMinZoomPreference(TransInfoInteractorImpl.MIN_ZOOM);
        googleMap.setIndoorEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        //Update camera position
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.192145, 5.725155)));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(16F));
        //Location
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        //  TODO  ask to enable location if clicked and no location
    }

    @Override
    public void addMapItem(MapItem mapItem) {
        Bitmap bitmap = BitmapUtil.getBitmap(getContext(), MapItemUtil.getIconResourceFromOperatorId(mapItem.getOperatorId()));
        Marker marker = mGoogleMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(mapItem.getLatitude(), mapItem.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        );
        marker.setTag(mapItem.getId());
    }

    @Override
    public void clear() {
        mGoogleMap.clear();
    }

    @Override
    public void showMessage(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleMap != null
                        && (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
                    //Enable location
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    public void animateMapTo(LatLng position) {
        if (position != null)
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(position), MARKER_ANIMATION_TIME_MS, null);
    }
}
