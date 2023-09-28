package com.xhstormr.app;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    public static Map<String, String> parseOptions(String args) {
        if (args == null) return new HashMap<>();

        return Pattern.compile("&")
            .splitAsStream(args)
            .map(o -> o.split("="))
            .peek(o -> o[1] = URLDecoder.decode(o[1], StandardCharsets.UTF_8))
            .collect(Collectors.toMap(o -> o[0], o -> o[1]));
    }
}
