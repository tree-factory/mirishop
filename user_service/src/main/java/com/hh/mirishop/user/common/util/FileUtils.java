package com.hh.mirishop.user.common.util;

import java.util.UUID;

public class FileUtils {

    public static String generateUniqueFileName(String originalFileName) {
        String extension = extractExtension(originalFileName);
        return UUID.randomUUID() + extension;
    }

    private static String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex >= 0) {
            return fileName.substring(dotIndex);
        }
        return ""; // 확장자가 없는 경우
    }
}
