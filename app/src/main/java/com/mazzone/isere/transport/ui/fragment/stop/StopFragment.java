package com.mazzone.isere.transport.ui.fragment.stop;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mazzone.isere.transport.R;
import com.mazzone.isere.transport.TransportApplication;
import com.mazzone.isere.transport.ui.model.HourItem;
import com.mazzone.isere.transport.util.AnswersUtil;
import com.mazzone.isere.transport.util.WordUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StopFragment extends Fragment implements StopFragmentView {

    @BindView(R.id.sheet_title) TextView mTitleTextView;
    @BindView(R.id.sheet_city) TextView cityTextView;
    @BindView(android.R.id.progress) ProgressBar mProgressBar;
    @BindView(android.R.id.empty) TextView mEmptyView;
    @BindView(android.R.id.list) RecyclerView mRecyclerView;

    @Inject StopPresenter mPresenter;
    private StopHoursAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inject Dagger
        TransportApplication
                .get(getActivity())
                .getAppComponent()
                .plus(new StopFragmentModule(this))
                .inject(this);
        //Retain instance
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stop, container, true);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(layoutManager);
            mAdapter = new StopHoursAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.bindView(this);
    }

    @Override
    public void showName(String name) {
        mTitleTextView.setText(WordUtil.capitalizeFully(name));
    }

    @Override
    public void showCity(String city) {
        cityTextView.setText(WordUtil.capitalizeFully(city));
    }

    @Override
    public void showLoading(boolean show) {
        mProgressBar.setVisibility((show) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    @UiThread
    public void showHours(List<HourItem> hourItems) {
        mAdapter.setListHours(hourItems);
    }

    @Override
    public void showEmpty(boolean show) {
        mEmptyView.setVisibility((show) ? View.VISIBLE : View.GONE);
    }

    public void setStopId(String stopId, String name, String city) {
        //Call content
        mPresenter.updateStop(stopId, name, city);

        //Log action
        AnswersUtil.logContentView(
                "Consult Stop Info",
                AnswersUtil.TYPE_TIME_TABLE,
                stopId, name);
    }
}
