package com.mazzone.isere.transport.ui.activity;

import com.mazzone.isere.transport.api.model.StopPoint;

import java.util.List;

public interface MainActivityView {
    void presentSearchResult(List<String> results);

    void presentHideSearchFeature();

    void presentStopSelection(StopPoint selectedStop);

    void presentSearchProgress(boolean show);
}
