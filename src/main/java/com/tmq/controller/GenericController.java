package com.tmq.controller;

import java.util.List;

public interface GenericController<T> {
    List<T> getAll();
}
