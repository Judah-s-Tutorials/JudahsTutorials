/*
private void openGoWrong(
    Supplier<Profile> supplier, 
    File file, 
    boolean expectChooser
)
{
    verify the target file exists
    expAction = fileMgr.getLastAction()
    formulate the Runnable that will drive a dedicated thread:
        Runnable runner = 
            () -> adHocProfile = supplier.get()
    pause to allow file chooser to post (if necessary)
    if ( expectChooser )
    {
        find the file chooser's text field
        enter the file name into the text field
        click on the open button
        expAction = APPROVE
    }
    verify an error dialog was not posted
    join the thread
    verify the value of adHocProfile
    validateState( file, true, expAction );
}
*/