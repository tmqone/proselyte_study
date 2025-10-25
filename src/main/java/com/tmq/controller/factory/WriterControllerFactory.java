package com.tmq.controller.factory;

import com.tmq.controller.WriterController;
import com.tmq.controller.WriterControllerImpl;
import com.tmq.mapper.WriterMapper;
import com.tmq.repository.jdbc.JdbcWriterRepositoryImpl;
import com.tmq.service.WriterServiceImpl;

public class WriterControllerFactory extends GenericControllerFactory {
    @Override
    public WriterController getController() {
        return new WriterControllerImpl(
                inputValidator,
                new WriterServiceImpl(JdbcWriterRepositoryImpl.getInstance()),
                new WriterMapper()
        );
    }
}
