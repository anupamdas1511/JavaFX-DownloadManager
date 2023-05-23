package org.example.config;

public class AppUtil {
    public static String formatFileSize(double fileSize) {
        if (fileSize < 1024) {
            return fileSize + " bytes";
        }
        double size = fileSize / 1024.0;
        if (size < 1024) {
            return String.format("%.2f KB", size);
        }
        size = size / 1024.0;
        if (size < 1024) {
            return String.format("%.2f MB", size);
        }
        size = size / 1024.0;
        if (size < 1024) {
            return String.format("%.2f GB", size);
        }
        return "Too Large";
    }

    public static double parseDouble(String str) {
        int index = str.length();
        for (int i=str.length()-1;i>=0;i--) {
            int ch = str.charAt(i)-'0';
            if (ch >= 0 && ch <= 9) {
                index = i;
                break;
            }
        }
        double size = Double.parseDouble(str.substring(0, index+1));

        if (index < str.length()-1) {
            String unit = str.substring(index+2);
            if (unit.equals("KB")) {
                size = size * 1024;
            } else if (unit.equals("MB")) {
                size = size * 1024 * 1024;
            } else if (unit.equals("GB")) {
                size = size * 1024 * 1024 * 1024;
            }
        }
        System.out.println("AppUtil: " + str.substring(0, index+1));
        return size;
    }
}
