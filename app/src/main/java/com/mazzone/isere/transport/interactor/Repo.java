package com.mazzone.isere.transport.interactor;

import java.util.List;

public interface Repo<T> {

    long count();

    void insert(T object);

    boolean exist(T object);

    List<T> getAll();
}
