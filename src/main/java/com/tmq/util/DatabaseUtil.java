package com.tmq.util;

import com.tmq.exception.GeneralException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseUtil {

    private static final int POOL_SIZE = 1;

    private static final List<String> methodsToCloseConnection = List.of("close");
    private static final List<Connection> CONNECTIONS = new ArrayList<>();
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
                connection.setAutoCommit(false);
                var proxyConnection = (Connection) Proxy.newProxyInstance(
                        DatabaseUtil.class.getClassLoader(),
                        new Class[]{Connection.class},
                        ((proxy, method, args) ->
                                methodsToCloseConnection.contains(method.getName()) ?
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
        return CONNECTIONS.getFirst();
    }

    public static PreparedStatement getStatement(String sql) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        return proxyPreparedStatement(connection, preparedStatement);
    }

    public static PreparedStatement getStatementWithGeneratedKeys(String sql) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        return proxyPreparedStatement(connection, preparedStatement);
    }

    private static PreparedStatement proxyPreparedStatement(Connection connection, Statement preparedStatement) {
        InvocationHandler handler = (proxy, method, args) -> {
            try {
                if (methodsToCloseConnection.contains(method.getName())) {
                    if (preparedStatement.getResultSet() != null) {
                        preparedStatement.getResultSet().close();
                    }
                    preparedStatement.close();
                    CONNECTIONS.add(connection);
                }
                return method.invoke(preparedStatement, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        return (PreparedStatement) Proxy.newProxyInstance(DatabaseUtil.class.getClassLoader(),
                new Class[]{PreparedStatement.class},
                handler);
    }
}
