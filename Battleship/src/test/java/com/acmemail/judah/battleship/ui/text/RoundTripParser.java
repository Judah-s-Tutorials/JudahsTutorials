package com.acmemail.judah.battleship.ui.text;

import static com.acmemail.judah.battleship.StatusMessages.HELP_END;
import static com.acmemail.judah.battleship.StatusMessages.TEXT_PROMPT;
import static com.acmemail.judah.battleship.StatusMessages.SUCCESS;
import static com.acmemail.judah.battleship.StatusMessages.FAILURE;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public class RoundTripParser
{
    public final List<String>           help;
    public final List<String>           prolog;
    public final List<String>           feedback; 
    public final List<CommandBucket>    buckets = new ArrayList<>();
    
    public RoundTripParser( List<String> rawFeedback )
    {
        int     rawFeedbackSize = rawFeedback.size();
        // sanity check; the feedback should include at least one prompt.
        assertTrue( rawFeedbackSize > 0 );
        
        // Find the help initial help output, if any. Assume it starts at
        // line 0, then look for HELP_END. If I can't find it
        // I'll substitute 0, which will change to one when I increment
        // it to point to the first line following help-end. If I can't
        // find help-end, the help-line list will be empty.
        int     helpStart       = 0;
        int     helpEnd         =
            IntStream.range( 0, rawFeedbackSize )
                .filter( i -> 
                    rawFeedback.get( i ).contains( HELP_END )
                )
                .findFirst().orElse( 0 ) + 1;
        
        // Find the prolog, which is everything between help-end
        // and the first prompt. It will often be an empty list.
        // If no prompt are found, it will extend to the end
        // of the feedback list
        int     prologEnd       =
            IntStream.range( 0, rawFeedbackSize )
                .filter( i -> 
                    rawFeedback.get( i ).startsWith( TEXT_PROMPT )
                )
                .findFirst().orElse( rawFeedbackSize );
        
        // The feedback is everything after the prolog; the first line
        // should be a prompt.
        
        help = rawFeedback.subList( helpStart, helpEnd );
        prolog = rawFeedback.subList( helpEnd, prologEnd );
        feedback = rawFeedback.subList( prologEnd, rawFeedbackSize );
        int     feedbackSize    = feedback.size();
        
        // Get the indices to all the prompts in the feedback.
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
            
            // Get all the lines between the current prompt and the next
            // one; if there is no next prompt, get everything to the
            // end of the feedback.
            List<String>    cmdLines    = feedback.subList( promptInx, end );
            CommandBucket   bucket      = getBucket( cmdLines );
            buckets.add( bucket );
        }
    }
    
    /**
     * Parse the real estate between two command prompts.
     * There is one special case: 
     * the first line after the command prompt
     * comes at the end of the command prompt,
     * so we split the first line into two.
     * Assumption:
     * the feedback associated with a command
     * can be split into four parts:
     * <ul>
     *      <li>The command prompt on the first line; followed by:</li>
     *      <li>0 to n lines of output; followed by:</li>
     *      <li>1 status line containing either SUCCESS or FAILURE; followed by:</li>
     *      <li>0 - n lines of suffix.
     * </ul>
     * 
     * @param feedback  the real estate to parse
     * 
     * @return  
     *      a Command bucket characterizing the four sections
     *      listed above 
     */
    private static CommandBucket getBucket( List<String> feedback )
    {
        List<String>    workingList     = new ArrayList<>();
        int             feedbackSize    = feedback.size();
        int             promptLen       = TEXT_PROMPT.length();
        
        // sanity check:
        assertTrue( feedbackSize > 0 );
        String          line1       = feedback.getFirst();
        // sanity check:
        assertTrue( line1.length() >= promptLen );
        // The prompt and the first line of output live on the
        // same line, so break them apart.
        workingList.add( line1.substring( 0, promptLen ) );
        workingList.add( line1.substring( promptLen ) );
        IntStream.range( 1, feedbackSize )
            .mapToObj( feedback::get )
            .forEach( workingList::add );
        
        // find the line with the status
        IntPredicate    statusPred  = i -> { 
            String  s = workingList.get( i );
            return s.contains( SUCCESS ) || s.contains( FAILURE );
        };
        int             listSize    = workingList.size();
        int             statusInx   = 
            IntStream.range( 1, listSize )
                .filter( statusPred::test  )
                .findFirst().orElse( -1 );
        // sanity check
        assertTrue( statusInx > 0 );
        
        String          prompt      = workingList.get( 0 );
        List<String>    output      = workingList.subList( 1, statusInx );
        String          status      = workingList.get( statusInx );
        List<String>    suffix      = 
            workingList.subList( statusInx + 1, listSize );
        CommandBucket   bucket      = 
            new CommandBucket( prompt, output, status, suffix );
        return bucket;
    }
}