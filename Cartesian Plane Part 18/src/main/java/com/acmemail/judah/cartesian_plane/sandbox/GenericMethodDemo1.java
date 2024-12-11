package com.acmemail.judah.cartesian_plane.sandbox;

import java.util.ArrayList;
import java.util.List;

public class GenericMethodDemo1
{

    public static void main(String[] args)
    {
        List<String>  strList = GenericMethodDemo1.<String>getList();
        List<Integer> intList = GenericMethodDemo1.<Integer>getList();
    }

    public static <T> List<T> getList()
    {
        List<T> list    = new ArrayList<>();
        return list;
    }
}
