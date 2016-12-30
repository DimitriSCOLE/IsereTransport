package com.mazzone.isere.transport.ui.fragment.map;

import com.mazzone.isere.transport.ui.model.MapItem;

public interface MapFragmentView {
    void addMapItem(MapItem mapItem);

    void showMessage(String message);

    void clear();
}
