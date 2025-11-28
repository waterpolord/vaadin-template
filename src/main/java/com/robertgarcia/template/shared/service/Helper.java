package com.robertgarcia.template.shared.service;

import org.springframework.stereotype.Service;

@Service
public class Helper {
    public static String asMoney(double v) {
        return new java.text.DecimalFormat("#,##0.##").format(v);
    }

    public static String identificationFormat(String raw) {
        raw = raw.replaceAll("[^0-9]", "");

        if (raw.length() > 13) raw = raw.substring(0, 11);

        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < raw.length(); i++) {
            formatted.append(raw.charAt(i));

            if (i == 2 || i == 9) formatted.append("-");
        }

        return formatted.toString();
    }

    public static String phoneFormat(String raw) {
        raw = raw.replaceAll("[^0-9]", "");
        if (raw.length() > 12) raw = raw.substring(0, 10);
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < raw.length(); i++) {
            formatted.append(raw.charAt(i));

            if (i == 2 || i == 5) formatted.append("-");
        }

        return formatted.toString();
    }

}
