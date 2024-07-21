package com.example.demo.common;

public class Method {
    public static String vaildRegexpString(String... required){
        StringBuilder builder = new StringBuilder();
        for (String regexp : required ){
            if (builder.isEmpty()){
                builder.append("(^");
            }else {
                builder.append("$|^");
            }
            builder.append(regexp);
        }
        builder.append("$)");
        return builder.toString();
    }
}
