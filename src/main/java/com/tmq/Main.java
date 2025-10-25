package com.tmq;

import com.tmq.util.DatabaseUtil;
import com.tmq.view.MainView;

import java.sql.SQLException;

public class Main {
    static void main() throws SQLException {
        MainView.getInstance().start();
    }
}
