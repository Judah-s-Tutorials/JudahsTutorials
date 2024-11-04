/*
private void cancelOperation(
    Supplier<?> supplier, 
    File file
)
{
    expFile = fileMgr.getCurrFile();
    expLastResult = fileMgr.getLastResult()
    formulate the Runnable that will drive a dedicated thread:
        Runnable    runner      = () -> supplier.get() 
    start dedicate thread to encapsulate the operation
    pause to allow file chooser to post
    
    find the file chooser's text field
    enter the file name into the text field
    click on the cancel button

    join the thread
    validateState( expFile, expResult, CANCEL )
}
*/