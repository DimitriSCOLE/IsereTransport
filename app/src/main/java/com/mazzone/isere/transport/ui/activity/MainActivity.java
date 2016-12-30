package com.mazzone.isere.transport.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.mazzone.isere.transport.R;
import com.mazzone.isere.transport.TransportApplication;
import com.mazzone.isere.transport.api.model.StopPoint;
import com.mazzone.isere.transport.ui.fragment.map.MapFragment;
import com.mazzone.isere.transport.ui.fragment.stop.StopFragment;
import com.mazzone.isere.transport.util.FontUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements MapFragment.OnStopSelectedListener, MainActivityView {
    private static final String STATE_SHEET = "state_sheet";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.horizontal_progress) MaterialProgressBar materialProgressBar;
    @BindView(R.id.bottom_sheet) View bottomSheet;

    @Inject MainPresenter mainPresenter;

    private MapFragment mapFragment;
    private StopFragment stopFragment;
    private BottomSheetBehavior bottomSheetBehavior;
    private PublishSubject<String> searchQuerySubject;
    private Subscription searchQuerySubjectSubscription;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //Inject Dagger
        TransportApplication
                .get(this)
                .getAppComponent()
                .plus(new MainActivityModule(this))
                .inject(this);

       // new FontUtil(this, FontUtil.ROBOTO_MONO_PATH).setTypeFace(title);
        setSupportActionBar(toolbar);

        bottomSheetBehavior = (BottomSheetBehavior) BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.peek_height));
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        stopFragment = (StopFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_sheet);
        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        if (savedInstanceState != null) {
            bottomSheetBehavior.setState(savedInstanceState.getInt(STATE_SHEET));
        }

        searchQuerySubject = PublishSubject.create();

        searchView.setArrowOnly(false);
        searchView.setVoice(false);

        searchAdapter = new SearchAdapter(this);
        searchAdapter.addOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mainPresenter.stopSelectionAtPosition(position);
            }
        });
        searchView.setAdapter(searchAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuerySubject.onNext(newText);
                return true;
            }
        });

        searchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }

            @Override
            public boolean onOpen() {
                onUnSelected();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        searchQuerySubjectSubscription = searchQuerySubject
                .debounce(500, TimeUnit.MILLISECONDS) //avoid one request per stroke
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                    }

                    @Override
                    public void onNext(String s) {
                        mainPresenter.searchQuery(s);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchQuerySubjectSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        // TODO: 13/11/2016 unbind presenter, avoid sending the view on the dagger injection, better bind and unbind
        // TODO: 13/11/2016 after bind and unbind on the presenter, put the publish subject on the presenter! and merge it to the current query call
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.close(true);//Close search before closing the app
        } else if (bottomSheetBehavior != null && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);//Collapse bottom sheet before closing the app
        } else if (bottomSheetBehavior != null && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);//Hide bottom sheet before closing the app
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchView.open(true, item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SHEET, bottomSheetBehavior.getState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStopSelected(String stopPointId, String name, String city) {
        mainPresenter.stopSelected();
        if (stopFragment != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            stopFragment.setStopId(stopPointId, name, city);
        }
    }

    @Override
    public void onUnSelected() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void showProgress(boolean isVisible) {
        materialProgressBar.setVisibility((isVisible) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void presentSearchResult(List<String> results) {
        List<SearchItem> suggestionsList = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            suggestionsList.add(i, new SearchItem(results.get(i)));
        }
        searchAdapter.setData(suggestionsList);
    }

    @Override
    public void presentHideSearchFeature() {
        searchView.close(true);
    }

    @Override
    public void presentStopSelection(StopPoint selectedStop) {
        onStopSelected(selectedStop.getLogicalStop().getId(), selectedStop.getName(), selectedStop.getLocality().getName());
        mapFragment.animateMapTo(new LatLng(selectedStop.getLatitude(), selectedStop.getLongitude()));
    }

    @Override
    public void presentSearchProgress(boolean show) {
        if (searchView != null)
            if (show) {
                searchView.showProgress();
            } else {
                searchView.hideProgress();
            }
    }
}
