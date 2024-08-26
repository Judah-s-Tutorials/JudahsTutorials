package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.Arrays;

public class ArraysHashEqualsDemo
{

    public static void main(String[] args)
    {
        int[][]   arr = { { 1,2,3 }, { 4, 5, 6 } };
        System.out.println( Arrays.deepHashCode( arr ) );
        arr[0][0]=7;
        arr[0][1]=8;
        arr[0][2]=9;
        System.out.println( Arrays.deepHashCode( arr ) );
    }

}
