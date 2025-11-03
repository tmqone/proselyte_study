package com.tmq.util;

import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.Writer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = initSessionFactory();
    }

    private static SessionFactory initSessionFactory() {
        return new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:54210/cli")
                .setProperty("hibernate.connection.username", "cli_user")
                .setProperty("hibernate.connection.password", "cli_pass")
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.connection.autocommit", "false")
                .addAnnotatedClass(Label.class)
                .addAnnotatedClass(Post.class)
                .addAnnotatedClass(Writer.class)
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
