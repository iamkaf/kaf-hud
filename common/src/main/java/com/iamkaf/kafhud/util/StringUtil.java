package com.iamkaf.kafhud.util;

public class StringUtil {
    /**
     * Converts a snake_case string into a readable sentence with proper capitalization.
     *
     * @param snakeCase the input string in snake_case format
     * @return a readable sentence with each word capitalized, or an empty string if the input is null or empty
     */
    public static String toReadableSentence(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) return "";
        StringBuilder result = new StringBuilder();
        for (String word : snakeCase.split("_")) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return result.toString().trim();
    }
}
