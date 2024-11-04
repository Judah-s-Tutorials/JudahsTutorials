/*
    private void saveGoRight( 
    BooleanSupplier supplier, 
    File file, 
    boolean expectChooser
)
{
    delete the target file
    expAction = fileMgr.getLastAction()
    formulate the Runnable that will drive a dedicated thread:
        Runnable    runner      = 
            () -> adHocResult = supplier.getAsBoolean();
    create and start the thread
    pause to allow file chooser to post (if necessary)
    if ( expectChooser )
    {
        find the file chooser's text field
        enter the file name into the text field
        click on the save button
        expAction = APPROVE
    }
    verify an error dialog was not posted
    join the thread
    verify the value of adHocResult
    verify the target file exists
    validateState( file, true, expAction );
}
*/