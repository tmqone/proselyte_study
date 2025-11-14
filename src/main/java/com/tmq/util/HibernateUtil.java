package com.tmq.util;

import com.tmq.exception.GeneralException;
import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public class HibernateUtil {
    @Getter
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
                .setProperty("hibernate.generate_statistics", "true")
                .setProperty("hibernate.current_session_context_class", "thread")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(File.class)
                .addAnnotatedClass(Event.class)
                .buildSessionFactory();
    }

    public static void initHibernate(){
        getSessionFactory().isClosed();
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }


    public static <T> T handleRequest(Supplier<T> supplier) {
        Session session = getSession();
        try {
            session.beginTransaction();
            T o = supplier.get();
            session.getTransaction().commit();
            return o;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
