package com.mazzone.isere.transport.ui.fragment.stop;

import com.mazzone.isere.transport.ui.model.HourItem;

import java.util.List;

public interface StopFragmentView {

    void showName(String name);

    void showCity(String city);

    void showLoading(boolean show);

    void showMessage(String message);

    void showHours(List<HourItem> hourItems);

    void showEmpty(boolean show);
}
