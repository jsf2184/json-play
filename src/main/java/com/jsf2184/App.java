package com.jsf2184;

import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IntStream.range(0,10).forEach(System.out::println);
        System.out.println( "Hello World!" );
    }
}
