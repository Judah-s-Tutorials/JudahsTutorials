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

function show( elID ) {
    console.log( elID );
    var ele = document.getElementById( elID );
    ele.style.display = "block";
} 

function showLetter( elID ) {
    var ele = document.getElementById( elID );
    ele.style.display = "block";
}

function linkShow( elID ) {
    console.log( elID );
    var ele = document.getElementById( elID );
    var letter = elID.charAt( 0 ).toLowerCase();
    var letterID = "letter-".concat( letter );
    show( letterID );
    ele.scrollIntoView();
} 