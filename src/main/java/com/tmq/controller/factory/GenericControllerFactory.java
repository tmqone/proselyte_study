package com.tmq.controller.factory;

import com.tmq.controller.GenericController;
import com.tmq.util.InputValidator;

public abstract class GenericControllerFactory {
    InputValidator inputValidator =  InputValidator.getInstance();
    public abstract GenericController<?> getController();
}
