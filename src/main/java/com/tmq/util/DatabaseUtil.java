package com.tmq.util;

import com.tmq.exception.GeneralException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseUtil {

    private static final int POOL_SIZE = 1;

    private static final ArrayBlockingQueue<Connection> CONNECTIONS = new ArrayBlockingQueue<>(5);
    private static boolean isInitialized = false;

    static {
        initDriver();
    }

    private static void initDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new GeneralException(e);
        }
    }

    private static void initConnection() {
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(
                        PropertiesUtil.get("DB.URL"),
                        PropertiesUtil.get("DB.USERNAME"),
                        PropertiesUtil.get("DB.PASSWORD")
                );
                var proxyConnection = (Connection) Proxy.newProxyInstance(
                        DatabaseUtil.class.getClassLoader(),
                        new Class[]{Connection.class},
                        ((proxy, method, args) -> method.getName().equals("close") ?
                                CONNECTIONS.add((Connection) proxy) : method.invoke(connection, args)));
                CONNECTIONS.add(proxyConnection);

            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    public static Connection getConnection() {
        if (!isInitialized) {
            initConnection();
            isInitialized = true;
        }

        try {
            return CONNECTIONS.take();
        } catch (InterruptedException e) {
            throw new GeneralException(e);
        }
    }
}
