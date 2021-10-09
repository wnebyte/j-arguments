package com.github.wnebyte.args;

public interface IWriter {

    void print(String out);

    void print(int out);

    void print(double out);

    void print(float out);

    void print(char out);

    void print(long out);

    void print(boolean out);

    void print(char[] out);

    void print(Object out);

    void println(String out);

    void println(int out);

    void println(double out);

    void println(float out);

    void println(char out);

    void println(long out);

    void println(boolean out);

    void println(char[] out);

    void println(Object out);

    void printerr(String out);
}