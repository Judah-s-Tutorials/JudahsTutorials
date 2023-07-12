package com.acmemail.judah.sandbox.sandbox;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class OptionalTest
{
    private static final Map<String, Optional<Object>>  optMapObj   = 
        new HashMap<>();
    private static final Map<String, Optional<?>>       optMapWC    = 
        new HashMap<>();
    
    public static void main(String[] args)
    {
        OptionalInt     optInt          = OptionalInt.of( 10 );
        OptionalInt     optIntEmpty     = OptionalInt.empty();
        OptionalDouble  optDbl          = OptionalDouble.of( 10.5 );
        OptionalDouble  optDblEmpty     = OptionalDouble.empty();
        Optional<Color> optColor        = Optional.of( Color.BLUE );
        Optional<Color> optColorEmpty   = Optional.of( Color.BLUE );
    }

}
