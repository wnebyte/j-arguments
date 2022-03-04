package com.github.wnebyte.jarguments.util;

import java.util.*;

/**
 * This class is a utility class for working with instance of {@link String}.
 */
public class Strings {

    /**
     * A read-only String that is empty.
     */
    public static final String EMPTY = "";

    /**
     * A read-only String consisting of a singular whitespace character.
     */
    public static final String WHITESPACE = " ";

    /**
     * Returns whether the specified <code>String</code> is equal to an empty String.
     * @param s a String.
     * @return <code>true</code> if the specified String is equal to an
     * empty String,
     * otherwise <code>false</code>.
     */
    public static boolean isEmpty(String s) {
        return (s != null) && (s.equals(EMPTY));
    }

    /**
     * Returns whether the specified <code>String</code> is <code>null</code> or equal to
     * an empty String.
     * @param s a String.
     * @return <code>true</code> if the specified String is <code>null</code> or equal to
     * an empty String,
     * otherwise <code>false</code>.
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null || isEmpty(s));
    }

    /**
     * Returns a new <code>String</code> filled with <code>len</code> number of elements of the specified
     * <code>char</code>.
     * @param c a char.
     * @param len a number.
     * @return a new <code>String</code> filled with len number of elements of the specified
     * char.
     */
    public static String fill(char c, int len) {
        char[] arr = new char[Math.max(len, 0)];
        Arrays.fill(arr, c);
        return new String(arr);
    }

    /**
     * Removes all occurrences of the elements contained within the specified <code>Collection</code> from
     * the specified <code>String</code>.
     * @param s a String.
     * @param c a Collection of Characters to be removed from the specified <code>s</code>.
     * @return the resulting String.
     */
    public static String removeAll(String s, Collection<Character> c) {
        if (s == null || c == null || c.isEmpty()) {
            return s;
        }
        StringBuilder builder = new StringBuilder();
        char[] arr = s.toCharArray();

        for (char character : arr) {
            if (c.contains(character)) {
                continue;
            }
            builder.append(character);
        }

        return builder.toString();
    }

    public static List<String> split(String s, char target, char discriminator) {
        List<String> elements = new ArrayList<>();
        if (isNullOrEmpty(s)) {
            return elements;
        }
        char[] arr = s.toCharArray();
        int start = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                String substring = s.substring(start, i);
                if (occurrences(substring, discriminator) % 2 == 0) {
                    elements.add(substring);
                    start = i + 1;
                }
            }
        }

        elements.add(s.substring(start));
        return elements;
    }

    public static List<String> split(String s, char target, Collection<Character> c) {
        List<String> elements = new ArrayList<>();
        if (isNullOrEmpty(s)) {
            return elements;
        }
        char[] arr = s.toCharArray();
        int start = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                String substring = s.substring(start, i);
                if (c.stream().allMatch(discriminator -> occurrences(substring, discriminator) % 2 == 0)) {
                    elements.add(substring);
                    start = i + 1;
                }
            }
        }

        elements.add(s.substring(start));
        return elements;
    }

    /**
     * Splits the specified <code>String</code> on each occurrence of a whitespace character
     * that is preceded by an even number of both single and double quotation characters.
     * @param s a String.
     * @return the result.
     */
    public static List<String> splitByWhitespace(String s) {
        return split(s, Chars.WHITESPACE, Chars.QUOTATION_CHARACTERS);
    }

    /**
     * Splits the specified <code>String</code> on each occurrence of a comma character that is preceded by
     * an even number of both single and double quotation characters.
     * @param s a String.
     * @return the result.
     */
    public static List<String> splitByComma(String s) {
        return split(s, Chars.COMMA, Chars.QUOTATION_CHARACTERS);
    }

    /**
     * Determines how many times the specified <code>char</code> occurs within the specified
     * <code>String</code>.
     * @param s a String.
     * @param c a char.
     * @return the number of times the specified char occurs within the specified
     * String.
     */
    public static int occurrences(String s, char c) {
        int count = 0;
        if (s == null) {
            return count;
        }
        for (char character : s.toCharArray()) {
            if (character == c) {
                count++;
            }
        }

        return count;
    }

    /**
     * Removes the first occurrence of the specified <code>first</code>, and the last
     * occurrence of the specified <code>last</code> if both are present within the specified
     * <code>String</code> and they do not represent the same char occupying the same index.
     * @param s a String.
     * @param first a char
     * @param last a char.
     * @return the resulting String.
     */
    public static String removeFirstAndLast(String s, char first, char last) {
        if (s == null) {
            return null;
        }
        int firstIndex = s.indexOf(first);
        int lastIndex = s.lastIndexOf(last);

        if ((firstIndex != -1) && (lastIndex != -1) && (firstIndex != lastIndex)) {
            char[] arr = new char[s.length() - 2];

            int j = 0;
            for (int i = 0; i < s.length(); i++) {
                if (i == firstIndex || i == lastIndex) {
                    continue;
                }
                arr[j++] = s.charAt(i);
            }
            return new String(arr);
        }

        return s;
    }

    /**
     * Removes the first occurrence of the specified <code>char</code>
     * if contained within the specified <code>String</code>.
     * @param s a String.
     * @param first a char.
     * @return the resulting String.
     */
    public static String removeFirst(String s, char first) {
        if (s == null) {
            return null;
        }
        int index = s.indexOf(first);

        if (index != -1) {
            char[] arr = new char[s.length() - 1];
            int j = 0;
            for (int i = 0; i < s.length(); i++) {
                if (i == index) {
                    continue;
                }
                arr[j++] = s.charAt(i);
            }
            return new String(arr);
        }

        return s;
    }

    /**
     * Determines whether the first char in the specified <code>String</code> is equal to the specified
     * <code>first</code> and the last char in the specified String is equal to the specified
     * <code>last</code>.
     * @param s a String.
     * @param first a char.
     * @param last a char.
     * @return <code>true</code> if the first char in the specified String is equal to the specified
     * first and the last char is equal to the specified last,
     * otherwise <code>false</code>.
     */
    public static boolean firstAndLastEquals(String s, char first, char last) {
        if (s == null || s.length() < 2) {
            return false;
        }
        int len = s.length();
        return (s.charAt(0) == first) && (s.charAt(len - 1) == last);
    }

    /**
     * Returns whether the specified <code>String</code> has a pair of first and last chars matching any of the
     * ones contained within the specified <code>Collection</code>.
     * @param s a String.
     * @param c a Collection of pairs.
     * @return <code>true</code> if the specified String has a pair of first and last chars matching any of the
     * ones contained within the specified Collection,
     * otherwise <code>false</code>.
     */
    public static boolean firstAndLastEqualsAny(String s, Collection<Pair<Character, Character>> c) {
        if (s == null || s.length() < 2 || c == null || c.isEmpty()) {
            return false;
        }
        for (Pair<Character, Character> pair : c) {
            if (firstAndLastEquals(s, pair.getFirst(), pair.getSecond())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the first element of the specified <code>Collection</code> that exists as a substring
     * within the specified <code>String</code>.
     * @param s a String.
     * @param c a Collection.
     * @return the first element of the specified Collection that exists as a substring
     * within the specified String,
     * or <code>null</code>.
     */
    public static String firstSubstring(String s, Collection<String> c) {
        if (s == null || c == null || c.isEmpty()) {
            return null;
        }
        for (String str : c) {
            if (s.equals(str) || s.contains(str)) {
                return str;
            }
        }

        return null;
    }

    /**
     * Returns the element with the greatest length from the specified <code>Collection</code> that also exists
     * as a substring within the specified <code>String</code>.
     * @param s a String.
     * @param c a Collection.
     * @return the element with the greatest length from the specified Collection that also exists
     * as a substring within the specified String,
     * or <code>null</code>.
     */
    public static String longestSubstring(String s, Collection<String> c) {
        if (s == null || c == null || c.isEmpty()) {
            return null;
        }
        String substring = null;

        for (String str : c) {
            if (s.equals(str) || s.contains(str)) {
                if (substring == null || str.length() > substring.length()) {
                    substring = str;
                }
            }
        }

        return substring;
    }

    /**
     * Determines whether the specified <code>String</code> and <code>Collection</code> intersect.
     * @param s a String.
     * @param c a Collection.
     * @return <code>true</code> if the specified String and Collection intersect,
     * otherwise <code>false</code>.
     */
    public static boolean intersects(String s, Collection<String> c) {
        return (firstSubstring(s, c) != null);
    }
}