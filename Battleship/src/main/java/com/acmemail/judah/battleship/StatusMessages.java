package com.acmemail.judah.battleship;

/**
 * This class collects error messages in a common place.
 */
public class StatusMessages
{
    public static final String  OUT_OF_BOUNDS       = 
        Messages.getString("StatusMessages.0"); //$NON-NLS-1$
    public static final String  INTERSECTS_SHIP     = 
        Messages.getString("StatusMessages.1"); //$NON-NLS-1$
    public static final String  GRID_EXISTS         =
        Messages.getString("StatusMessages.2"); //$NON-NLS-1$
    public static final String  ALREADY_DEPLOYED    =
        Messages.getString("StatusMessages.3"); //$NON-NLS-1$
    public static final String  MALFUNCTION         =
        Messages.getString("StatusMessages.4"); //$NON-NLS-1$
    public static final String  NOT_SETUP           =
        Messages.getString("StatusMessages.5"); //$NON-NLS-1$
    public static final String  NOT_CONFIG           =
        Messages.getString("StatusMessages.6"); //$NON-NLS-1$
    public static final String  NOT_CONFIG_COMPLETE  =
        Messages.getString("StatusMessages.7"); //$NON-NLS-1$
    public static final String  INVALID_PROTO_SHIP   =
        Messages.getString("StatusMessages.8"); //$NON-NLS-1$
    public static final String  NOT_DEPLOYED        =
        Messages.getString("StatusMessages.9"); //$NON-NLS-1$
    public static final String  PARSE_FAILED        =
        Messages.getString("StatusMessages.10"); //$NON-NLS-1$
    public static final String  SUCCESS             =
        Messages.getString("StatusMessages.11"); //$NON-NLS-1$
    public static final String  INVALID_COL         =
        Messages.getString("StatusMessages.12"); //$NON-NLS-1$
    public static final String  INVALID_ROW         =
        Messages.getString("StatusMessages.13"); //$NON-NLS-1$
    public static final String  FILE_NOT_FOUND      =
        Messages.getString("StatusMessages.14"); //$NON-NLS-1$
    public static final String  INVALID_DIM_SPEC    =
        Messages.getString("StatusMessages.15"); //$NON-NLS-1$
    public static final String  INVALID_ROW_COUNT   =
        Messages.getString("StatusMessages.16"); //$NON-NLS-1$
    public static final String  INVALID_COL_COUNT   =
        Messages.getString("StatusMessages.17"); //$NON-NLS-1$
    public static final String  INVALID_TYPE        =
        "Invalid type";
    public static final String  INVALID_BREADTH     =
        "Invalid breadth";
    public static final String  INVALID_LENGTH      =
        "Invalid length";
    public static final String  INVALID_P_RECORD    =
        "Invalid provisioning record";
    public static final String  DUP_SHIP_TYPE       =
        "Duplicate ship type";
    public static final String  SHIP_TYPE_NOT_FOUND =
        "Ship type not found";
    public static final String  INVALID_P_COMMAND =
        "Invalid provisioning command";
}
