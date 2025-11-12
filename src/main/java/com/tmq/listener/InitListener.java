package com.tmq.listener;


import com.tmq.util.FileUtil;
import com.tmq.util.FlywayUtil;
import com.tmq.util.HibernateUtil;
import com.tmq.util.PropertiesUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PropertiesUtil.loadProperties();
        HibernateUtil.initHibernate();
        FileUtil.initUploadFolder();
        FlywayUtil.migrate();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
