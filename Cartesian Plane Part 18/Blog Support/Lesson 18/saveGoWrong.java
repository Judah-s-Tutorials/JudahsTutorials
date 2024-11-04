/*
private void saveGoWrong( 
    BooleanSupplier supplier, 
    File file, 
    boolean expectChooser
)
{
    expAction = fileMgr.getLastAction()
    formulate the Runnable that will drive a dedicated thread:
        Runnable runner = 
            () -> adHocResult = supplier.getAsBoolean();
    pause to allow file chooser to post (if necessary)
    if ( expectChooser )
    {
        find the file chooser's text field
        enter the file name into the text field
        click on the save button
        expAction = APPROVE
    }
    verify an error dialog was posted
    join the thread
    verify the value of adHocResult
    validateState( file, true, expAction );
}
*/