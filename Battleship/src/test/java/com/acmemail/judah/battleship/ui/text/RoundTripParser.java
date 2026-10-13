package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.TEXT_PROMPT;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RoundTripParser
{
    public final List<String>           prolog;
    public final List<String>           feedback; 
    public final List<CommandBucket>    buckets = new ArrayList<>();
    
    public RoundTripParser( List<String> rawFeedback )
    {
        int     rawFeedbackSize = rawFeedback.size();
        int     promptLen       = TEXT_PROMPT.length();
        int     firstPrompt     =
            IntStream.range( 0, rawFeedbackSize )
                .filter( i -> 
                    rawFeedback.get( i ).startsWith( TEXT_PROMPT )
                )
                .findFirst().orElse( rawFeedbackSize );
        
        prolog = rawFeedback.subList( 0, firstPrompt );
        feedback = rawFeedback.subList( firstPrompt, rawFeedbackSize );
        int     feedbackSize    = feedback.size();
        int[]   prompts         = 
            IntStream.range( 0, feedbackSize )
                .filter( i -> 
                    feedback.get( i ).startsWith( TEXT_PROMPT )
                )
                .toArray();

        for ( int inx = 0 ; inx < prompts.length ; ++inx )
        {
            int             promptInx   = prompts[inx];
            int             end         = inx < prompts.length - 1 ? 
                prompts[inx + 1] : feedbackSize;
            String          firstLine   = feedback.get( promptInx );
            String          prompt      = 
                firstLine.substring( 0, promptLen );
            String          status      = firstLine.substring( promptLen );
            List<String>    suffix      = feedback.subList( promptInx + 1, end );
            List<String>    output      = new ArrayList<>();
            buckets.add( new CommandBucket( prompt, status, output, suffix ) );
        }
    }
}