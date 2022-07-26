package com.example.combinedproject.Others;

/** This interface is for the observable object. RowDialog shall implement it */

public interface Observable {
    public void addObserver(MainAdapter adapter);
    public void notifyChanges();
}
