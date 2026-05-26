package com.jerme.sis.util;

@FunctionalInterface
public interface Checker<T> {

    boolean check(T t);

}