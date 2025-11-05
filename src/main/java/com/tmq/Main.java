package com.tmq;

import com.tmq.util.HibernateUtil;
import com.tmq.view.MainView;

public class Main {
    static void main(String[] args) {
        HibernateUtil.initHibernate();
        MainView.getInstance().start();
    }
}
