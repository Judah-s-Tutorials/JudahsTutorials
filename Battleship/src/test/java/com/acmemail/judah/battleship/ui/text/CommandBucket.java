package com.acmemail.judah.battleship.ui.text;

import java.util.List;

public record CommandBucket( 
    String prompt,
    String status,
    List<String> output,
    List<String> suffix
)
{
}