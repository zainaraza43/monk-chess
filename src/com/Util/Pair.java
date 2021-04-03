/*
 * Comp 2800 Java3D Final Project
 * Usman Farooqi 105219637
 * Jagraj Aulakh
 * Ghanem Ghanem
 * Ali-Al-Timimy
 * Zain Raza
 * Pair.java
 */
package com.Util;

public class Pair <T, K>{

    private T first;
    private K second;

    public Pair(T first, K second){
       this.first = first;
       this.second = second;
    }

    public K getSecond() {
        return second;
    }
    public T getFirst() {
        return first;
    }
}
