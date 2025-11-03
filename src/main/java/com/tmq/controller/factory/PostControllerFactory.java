package com.tmq.controller.factory;

import com.tmq.controller.PostController;
import com.tmq.controller.PostControllerImpl;
import com.tmq.mapper.PostMapper;
import com.tmq.repository.hibernate.HibernateLabelRepositoryImpl;
import com.tmq.repository.hibernate.HibernatePostRepositoryImpl;
import com.tmq.service.LabelServiceImpl;
import com.tmq.service.PostServiceImpl;

public class PostControllerFactory extends GenericControllerFactory {
    @Override
    public PostController getController() {
        return new PostControllerImpl(
                new PostServiceImpl(
                        new HibernatePostRepositoryImpl(),
                        new LabelServiceImpl(new HibernateLabelRepositoryImpl())
                ),
                new PostMapper(),
                inputValidator
        );
    }
}
