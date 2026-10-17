package com.acmemail.judah.battleship.sandbox;

import java.util.List;
import java.util.function.Predicate;

public class IsLabeledRowDemo
{

    public IsLabeledRowDemo()
    {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        String              sample          = " A-------------------";
        Predicate<String>   isLabeledRow    = 
            s -> s.matches( "^\\s*[A-Z]+.*" );
        System.out.println( sample.matches( "^\\s*[A-Z]+.*" ) );
        List<String>        startList       =
            List.of( 
                "*********************",
                " A-------------------",
                "*********************",
                " B-------------------",
                "*********************",
                " C-------------------",
                "*********************",
                "AA-------------------",
                "*********************",
                "AB-------------------",
                "*********************",
                "AC-------------------"
            );
        List<String>        filteredList    =
            startList.stream()
                .filter( isLabeledRow::test )
                .peek( System.out::println )
                .toList();
        System.out.println( "================" );
        filteredList.forEach( System.out::println );
    }

}
