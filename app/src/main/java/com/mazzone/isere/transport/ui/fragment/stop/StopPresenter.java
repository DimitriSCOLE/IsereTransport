package com.mazzone.isere.transport.ui.fragment.stop;

public interface StopPresenter {

    void bindView(StopFragmentView stopView);

    void updateStop(String StopId, String name, String city);
}
