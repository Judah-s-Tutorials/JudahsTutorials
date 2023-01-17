package com.acmemail.judah.cartesian_plane.sandbox;

import com.acmemail.judah.cartesian_plane.CPConstants;
import com.acmemail.judah.cartesian_plane.PropertyManager;

public class PropertyManagerDemo
{
    public static void main(String[] args)
    {
        PropertyManager pMgr    = PropertyManager.INSTANCE;
        System.out.println( pMgr.asFloat( CPConstants.TIC_MAJOR_WEIGHT_PN ) );
        System.out.println( pMgr.asFloat( CPConstants.TIC_MAJOR_LEN_PN ) );
        System.out.println( pMgr.asColor( CPConstants.TIC_MAJOR_COLOR_PN ) );
    }
}
