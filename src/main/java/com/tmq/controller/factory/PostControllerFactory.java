package com.tmq.controller.factory;

import com.tmq.controller.PostController;
import com.tmq.controller.PostControllerImpl;
import com.tmq.mapper.PostMapper;
import com.tmq.repository.LabelRepositoryImpl;
import com.tmq.repository.PostRepositoryImpl;
import com.tmq.repository.WriterRepositoryImpl;
import com.tmq.service.PostServiceImpl;

public class PostControllerFactory extends GenericControllerFactory {
    @Override
    public PostController getController() {
        return new PostControllerImpl(
                new PostServiceImpl(
                        LabelRepositoryImpl.getInstance(),
                        WriterRepositoryImpl.getInstance(),
                        PostRepositoryImpl.getInstance()
                ),
                new PostMapper(),
                inputValidator
        );
    }
}
