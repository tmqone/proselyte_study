package com.tmq.controller.factory;

import com.tmq.controller.LabelController;
import com.tmq.controller.LabelControllerImpl;
import com.tmq.mapper.LabelMapper;
import com.tmq.repository.jdbc.JdbcLabelRepositoryImpl;
import com.tmq.service.LabelServiceImpl;

public class LabelControllerFactory extends GenericControllerFactory {
    @Override
    public LabelController getController() {
        return new LabelControllerImpl(inputValidator,
                new LabelServiceImpl(JdbcLabelRepositoryImpl.getInstance()),
                new LabelMapper());
    }
}
