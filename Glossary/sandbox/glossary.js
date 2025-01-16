function hideShow( elID ) {
    console.log( elID );
    var ele = document.getElementById( elID );
    var display = ele.style.display;
    if (display === "none" || display === "") {
        ele.style.display = "block";
    } 
    else {
        ele.style.display = "none";
    }
} 