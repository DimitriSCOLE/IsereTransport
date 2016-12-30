package com.mazzone.isere.transport.ui.activity;

public interface MainPresenter {
    void searchQuery(String query);

    void stopSelected();

    void stopSelectionAtPosition(int position);
}
