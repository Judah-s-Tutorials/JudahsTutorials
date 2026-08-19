package com.acmemail.judah.battleship;

/**
 * This class manages the state progression
 * through game configuration. 
 * The states are:
 * <ol>
 *      <li>
 *          Setup in  progress.<br>
 *          In this state, 
 *          initial game property configuration
 *          is occurring.
 *          This includes
 *          Establishing the dimensions of the game grid,
 *          the number of players,
 *          and the list of ships to be deployed.
 *      </li>
 *      <li>
 *          Configuration in progress.<br>
 *          In this state, 
 *          the dimensions of the grid,
 *          the number of opponents,
 *          and the types of ships to be deployed
 *          by each opponent
 *          are set and cannot be changed.
 *          Specific ships are configured and deployed
 *          on the home grid.
 *      </li>
 *      <li>
 *          Configuration complete.<br>
 *          All ships are deployed on the home grid,
 *          and their configuration may not be changed.
 *          The game is ready to begin.
 *      </li>
 *      <li>
 *          Game over.<br>
 *          The game is complete.
 *          No more moves can be made.
 *      </li>
 * </ol>
 * <p>
 * Once the game has progressed from one state to the next,
 * the progression cannot be undone.
 */
public class Configurator
{
    private static int  state   = Constants.SETUP;
    
    /**
     * Default constructor; not used.
     */
    private Configurator()
    {
        // not used
    }
    
    /**
     * Gets the current state of the game.
     * Possible states are:
     * <ul>
     * <li>{@linkplain Constants#SETUP} </li>
     * <li>{@linkplain Constants#CONFIG} </li>
     * <li>{@linkplain Constants#CONFIG_COMPLETE} </li>
     * <li>{@linkplain Constants#GAME_OVER} </li>
     * </ul>
     * 
     * @return  the current state of the game
     */
    public static int getState()
    {
        return state;
    }
    
    /**
     * Places the game in the next sequential state.
     * The new state is returned.
     * Does nothing if the current state is GAME_OVER.
     * 
     * @return  the new state of the game
     */
    public static int nextState()
    {
        if ( state < Constants.GAME_OVER )
            state++;
        return state;
    }
    
    /**
     * Returns true if the current state of the game is 
     * {@linkplain Constants#SETUP}.
     * 
     * @return true if the game is currently in the SETUP state
     */
    public static boolean isSetup()
    {
        boolean result  = state == Constants.SETUP;
        return result;
    }
    
    /**
     * Returns true if the current state of the game is 
     * {@linkplain Constants#CONFIG}.
     * 
     * @return true if the game is currently in the CONFIG state
     */
    public static boolean isConfig()
    {
        boolean result  = state == Constants.CONFIG;
        return result;
    }
    
    /**
     * Returns true if the current state of the game is 
     * {@linkplain Constants#CONFIG_COMPLETE}.
     * 
     * @return true if the game is currently in the CONFIG_COMPLETE state
     */
    public static boolean isConfigComplete()
    {
        boolean result  = state == Constants.CONFIG_COMPLETE;
        return result;
    }
    
    /**
     * Returns true if the current state of the game is 
     * {@linkplain Constants#SETUP}.
     * 
     * @return true if the game is currently in the GAME_OVER state
     */
    public static boolean isGameOver()
    {
        boolean result  = state == Constants.GAME_OVER;
        return result;
    }
}
