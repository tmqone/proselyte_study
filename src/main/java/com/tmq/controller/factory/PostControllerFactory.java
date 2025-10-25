package com.tmq.controller.factory;

import com.tmq.controller.PostController;
import com.tmq.controller.PostControllerImpl;
import com.tmq.mapper.PostMapper;
import com.tmq.repository.jdbc.JdbcLabelRepositoryImpl;
import com.tmq.repository.jdbc.JdbcPostRepositoryImpl;
import com.tmq.repository.jdbc.JdbcWriterRepositoryImpl;
import com.tmq.service.LabelServiceImpl;
import com.tmq.service.PostServiceImpl;

public class PostControllerFactory extends GenericControllerFactory {
    @Override
    public PostController getController() {
        return new PostControllerImpl(
                new PostServiceImpl(
                        JdbcPostRepositoryImpl.getInstance(),
                        new LabelServiceImpl(JdbcLabelRepositoryImpl.getInstance())
                ),
                new PostMapper(),
                inputValidator
        );
    }
}
