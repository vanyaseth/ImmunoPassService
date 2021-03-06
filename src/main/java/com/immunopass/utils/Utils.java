package com.immunopass.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<String> getFileLines(MultipartFile file) {
        BufferedReader br;
        List<String> result = new ArrayList<>();
        try {

            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file lines: " + e.getLocalizedMessage());
        }
        return result;
    }

    public static String getRandomString(int len) {
        return RandomStringUtils.randomAlphabetic(len).toUpperCase();
    }
}
