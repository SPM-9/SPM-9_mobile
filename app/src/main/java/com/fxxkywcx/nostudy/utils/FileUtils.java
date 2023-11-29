package com.fxxkywcx.nostudy.utils;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Scanner;

public class FileUtils {
    static final String[] unit = {"B", "KB", "MB", "GB", "TB"};
    @NotNull
    public static String sizeToString(long fileSize, int accuracy) {
        int cnt = 0;
        double size = fileSize;
        while (size >= 1024) {
            size /= 1024;
            cnt++;
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(accuracy);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String res = nf.format(size);
        res += unit[cnt];
        return res;
    }
}
