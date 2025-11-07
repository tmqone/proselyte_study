package com.tmq.util;

import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;
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
                .setProperty("hibernate.connection.driver_class", PropertiesUtil.get("hibernate.connection.driver_class"))
                .setProperty("hibernate.connection.url", PropertiesUtil.get("hibernate.connection.url"))
                .setProperty("hibernate.connection.username", PropertiesUtil.get("hibernate.connection.username"))
                .setProperty("hibernate.connection.password", PropertiesUtil.get("hibernate.connection.password"))
                .setProperty("hibernate.show_sql", PropertiesUtil.get("hibernate.show_sql"))
                .setProperty("hibernate.format_sql", PropertiesUtil.get("hibernate.format_sql"))
                .setProperty("hibernate.connection.autocommit", PropertiesUtil.get("hibernate.connection.autocommit"))
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(File.class)
                .addAnnotatedClass(Event.class)
                .buildSessionFactory();
    }

    public static void initHibernate(){
        getSessionFactory().isClosed();
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
