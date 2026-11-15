package com.acmemail.judah.battleship.ui.awt;

import java.awt.Color;

public class UIConstants
{
    /** The default text color for valid values. */
    public static final Color   VALID_TEXT_COLOR            =
        Color.BLACK;
    /** The default text color for invalid values. */
    public static final Color   INVALID_TEXT_COLOR          =
        Color.RED;
    
    
    /** The default title for the setup phase main window. */
    public static final String  SETUP_WINDOW_TITLE          =
        "Setup Configuration Before Game";
    /** The default title for the ship type dialog. */
    public static final String  SHIP_TYPE_DIALOG_TITLE      =
        "Register a Ship Type";
    public static final String  OK_LABEL                    = 
        "OK";
    public static final String  CANCEL_LABEL                = 
        "Cancel";
    public static final String  LENGTH_LABEL                = 
        "Length:";
    public static final String  BREADTH_LABEL               = 
        "Breadth:";
    public static final String  TYPE_NAME_LABEL             = 
        "Type Name:";
    
    /** The title for the Players list. */
    public static final String  PLAYERS_LABEL               =
        "Players";
    /** The title for the to-regester list. */
    public static final String  TO_REGISTER_LABEL           =
        "To Register";
    /** The title for the to-deploy list. */
    public static final String  TO_DEPLOY_LABEL             =
        "To Deploy";
    /** Text for an "add" component, for example, a pushbutton. */
    public static final String  ADD_LABEL                   =
        "Add";
    /** Text for a "delete selected" component, for example, a pushbutton. */
    public static final String  DELETE_SELECTED             =
        "Delete Selected";
    /** Text for a "copy selected" component, for example, a pushbutton. */
    public static final String  COPY_SELECTED               =
        "Copy Selected";
    /** Glyph used to indicate the direction of a "copy selected" operation. */
    public static final String  ARROW_GLYPH                 =
        "→";
    /** Text for a "add defaults" component, for example, a pushbutton. */
    public static final String  ADD_DEFAULTS                =
        "Add Defaults";
    /** Text for a "a new" component, for example, a pushbutton. */
    public static final String  ADD_NEW                     =
        "Add New";
    /** Text for a "rows" component. */
    public static final String  ROW_LABEL                   =
        "Rows";
    /** Text for a "columns" component. */
    public static final String  COL_LABEL                   =
        "Columns";
    
    /***********************************************
     * Names for components on register-type dialog;
     * all must have prefix RDN
     ***********************************************/

    /** Length component. */
    public static final String  RDN_LENGTH              = "length";
    /** Breadth component. */
    public static final String  RDN_BREADTH             = "breadth";
    /** Type name component. */
    public static final String  RDN_NAME                = "name";
    /** Messages component. */
    public static final String  RDN_MESSAGES            = "messages";
    /** OK button */
    public static final String  RDN_OK_BUTTON           = "okButton";
    /** Cancel button */
    public static final String  RDN_CANCEL_BUTTON       = "cancelButton";
    
    /*****************************************
     * Names for components on setup dialog;
     * all must have prefix SDN
     *****************************************/
    
    /** Num-rows component. */
    public static final String  SDN_ROWS                = "rows";
    /** Num-columns component. */
    public static final String  SDN_COLS                = "cols";
    
    /** Players-list component. */
    public static final String  SDN_PLAYERS_LIST        = "playersList";
    /** Players-add text component. */
    public static final String  SDN_PLAYERS_ADD_TEXT    = "playersAddText";
    /** Players-add button. */
    public static final String  SDN_PLAYERS_ADD_BUTTON  = "playersAddButton";
    /** Players-delete button. */
    public static final String  SDN_PLAYERS_DEL_BUTTON  = "playersDelButton";
    
    /** To-register list */
    public static final String  SDN_REG_LIST            = "regList";
    /** To-register delete button. */
    public static final String  SDN_REG_DEL_BUTTON      = "regDelButton";
    /** To-register add-defaults button. */
    public static final String  SDN_REG_ADD_DEF_BUTTON  = "regAddDefButton";
    /** To-register add-new button. */
    public static final String  SDN_REG_ADD_NEW_BUTTON  = "regAddNewButton";
    
    /** To-deploy list */
    public static final String  SDN_DEP_LIST            = "depList";
    /** To-deploy delete button. */
    public static final String  SDN_DEP_DEL_BUTTON      = "depDelButton";
    
    /** Copy-selected button */
    public static final String  SDN_COPY_BUTTON         = "copyButton";
    /** OK button */
    public static final String  SDN_OK_BUTTON           = "okButton";
    /** Cancel button */
    public static final String  SDN_CANCEL_BUTTON       = "cancelButton";
    /** Message text-area */
    public static final String  SDN_MESSAGES            = "messages";

    /**
     * Default constructor; not used
     */
    private UIConstants()
    {
        // not used
    }
}
