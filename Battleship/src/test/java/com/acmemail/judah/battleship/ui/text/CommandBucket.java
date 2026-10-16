package com.acmemail.judah.battleship.ui.text;

import java.util.List;

public record CommandBucket( 
    String       prompt,
    List<String> output,
    String       status,
    List<String> suffix
)
{
}