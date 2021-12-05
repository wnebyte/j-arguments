package com.github.wnebyte.jarguments.util;

import java.util.*;

/**
 * This class declares utility methods for working with Strings.
 */
public class Strings {

    /**
     * Removes occurrences of the specified characters if present from the specified String <code>s</code>.
     * @param s to be normalized.
     * @param exclude characters to be removed from the specified String.
     * @return the result
     */
    public static String exclude(final String s, final Collection<Character> exclude) {
        if ((s == null) || (exclude == null) || (exclude.isEmpty())) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        char[] arr = s.toCharArray();

        for (char c : arr) {
            if (exclude.contains(c)) {
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    /**
     * Returns whether the specified String <code>s</code> <code>equals</code> an empty String.
     * @param s the String.
     * @return <code>true</code> if the specified String <code>s</code> is <code>equal</code> to an
     * empty String, otherwise <code>false</code>.
     */
    public static boolean isEmpty(final String s) {
        return (s != null) && (s.equals(""));
    }

    /**
     * Returns whether the specified String <code>s</code> is <code>null</code> or <code>equals</code>
     * an empty String.
     * @param s the String.
     * @return <code>true</code> if the specified String is either <code>null</code> or <code>equals</code>
     * an empty String, otherwise <code>false</code>.
     */
    public static boolean isNullOrEmpty(final String s) {
        return (s == null) || (isEmpty(s));
    }

    /**
     * Splits the specified String <code>s</code> on occurrences of the specified char <code>target</code>,
     * if the <code>target</code> char is preceded by an even number of occurrences of the specified char
     * <code>prevChar</code>.
     * @param s to be split.
     * @param target delimiter to split on.
     * @param prevChar has to occur an even number of times in the substring ending at
     * the index of <code>target</code> for the split to occur.
     * @return the result.
     */
    public static List<String> split(final String s, final char target, final char prevChar) {
        List<String> elements = new ArrayList<>();
        if (isNullOrEmpty(s)) { return elements; }
        char[] arr = s.toCharArray();
        int start = 0;

        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] == target) && (occurrences(s.substring(start, i), prevChar) % 2 == 0)) {
                elements.add(s.substring(start, i));
                start = i + 1;
            }
        }
        elements.add(s.substring(start));
        return elements;
    }

    /**
     * Splits the specified String <code>s</code> on occurrences of the specified <code>target</code> char where
     * it is preceded by an even number of each and every characters present in the specified <code>collection</code>.
     * @param s to be split.
     * @param target delimiter to split on.
     * @param collection every element has to occur an even number of times in the substring ending at
     * the index of <code>target</code> for the split to occur.
     * @return the result.
     */
    public static List<String> split(final String s, final char target, final Collection<Character> collection) {
        List<String> elements = new ArrayList<>();
        if (isNullOrEmpty(s)) { return elements; }
        char[] arr = s.toCharArray();
        int start = 0;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                String substring = s.substring(start, i);
                boolean even = collection.stream()
                        .allMatch(prevChar -> occurrences(substring, prevChar) % 2 == 0);
                if (even) {
                    elements.add(substring);
                    start = i + 1;
                }
            }
        }
        elements.add(s.substring(start));
        return elements;
    }

    /**
     * Splits the specified String <code>s</code> on each occurrence of a whitespace character
     * that is preceded by an even number of single and double quotation characters.
     * @param s to be split.
     * @return the result.
     */
    public static List<String> splitByWhitespace(final String s) {
        return split(s, ' ', Arrays.asList('"', '\''));
    }

    /**
     * Splits the specified String <code>s</code> on each occurrence of a comma character that is preceded by
     * an even number of single and double quotation characters.
     * @param s to be split.
     * @return the result.
     */
    public static List<String> splitByComma(final String s) {
        return split(s, ',', Arrays.asList('"', '\''));
    }

    /**
     * Counts and returns the number of times the specified char <code>c</code> occurs
     * in the specified String <code>s</code>.
     */
    public static int occurrences(final String s, final char c) {
        int count = 0;
        if (s == null) { return count; }
        for (char character : s.toCharArray()) {
            if (character == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * Removes the first occurrence of the specified char <code>first</code>, and the last
     * occurrence of the specified char <code>last</code>, if both are present within the specified
     * String <code>s</code>, and they do not represent the same char occupying the same index.
     * @param s the String.
     * @param first the char whose first occurrence is to be removed.
     * @param last the char whose last occurrence is to be removed.
     * @return the result.
     */
    public static String removeFirstAndLast(final String s, final char first, final char last) {
        if (s == null) { return null; }
        int firstIndex = s.indexOf(first);
        int lastIndex = s.lastIndexOf(last);

        if ((firstIndex != -1) && (lastIndex != -1) && (firstIndex != lastIndex)) {
            char[] array = new char[s.length() - 2];
            int j = 0;
            for (int i = 0; i < s.length(); i++) {
                if ((i == firstIndex) || (i == lastIndex)) {
                    continue;
                }
                array[j++] = s.charAt(i);
            }
            return new String(array);
        }
        return s;
    }

    public static String removeFirst(final String s, final char first) {
        if (s == null) { return null; }
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

    public static boolean firstAndLastMatches(final String s, final char first, final char last) {
        if ((s == null) || (s.length() < 2)) { return false; }
        int len = s.length();
        return (s.charAt(0) == first) && (s.charAt(len - 1) == last);
    }

    /**
     * Returns whether the specified String <code>s</code> has a pair of first and last chars matching any of the
     * specified pairs.
     */
    public static boolean firstAndLastMatchesAny(final String s, final Collection<Pair<Character, Character>> chars) {
        if ((s == null) || (s.length() < 2) || (chars == null) || (chars.isEmpty())) { return false; }
        int len = s.length();
        for (Pair<Character, Character> pair : chars) {
            boolean matches = firstAndLastMatches(s, pair.getFirst(), pair.getLast());
            if (matches) {
                return true;
            }
        }
        return false;
    }

    public static String firstSubstring(final String s, final Collection<String> substrings) {
        if ((s == null) || (substrings == null) || (substrings.isEmpty())) { return null; }
        String val = null;
        int min = Integer.MAX_VALUE;

        int i = 0;
        for (String str : substrings) {
            if ((s.equals(str)) || (s.contains(str))) {
                if (i < min) {
                    min = i;
                    val = str;
                }
            }
        }
        return val;
    }

    public static boolean intersects(final String s, final Collection<String> substrings) {
        return firstSubstring(s, substrings) != null;
    }

    public static String fill(final char c, final int len) {
        char[] arr = new char[len];
        Arrays.fill(arr, c);
        return new String(arr);
    }
}