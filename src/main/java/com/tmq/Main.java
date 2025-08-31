package com.tmq;
import com.tmq.util.FilesPath;
import com.tmq.util.FilesUtil;
import com.tmq.view.MainView;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FilesUtil.initFiles();
        MainView.getInstance().start();
    }
}