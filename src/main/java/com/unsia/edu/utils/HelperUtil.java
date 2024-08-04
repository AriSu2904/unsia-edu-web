package com.unsia.edu.utils;

import org.springframework.stereotype.Component;

@Component
public class HelperUtil {

    public static String extractFullName (String firstName, String lastName) {
        return firstName + " " + lastName;
    }

}
