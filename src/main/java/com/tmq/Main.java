package com.tmq;
import com.tmq.util.FilesUtil;
import com.tmq.view.MainView;

public class Main {
    public static void main(String[] args){
        FilesUtil.initFiles();
        MainView.getInstance().start();
    }
}