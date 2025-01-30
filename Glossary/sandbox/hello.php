<?
    // php require( "functions.php" );
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Glossary of Java Terms</title>
    <link rel="stylesheet" href="glossary.css"> 
    <script src="glossary.js"></script> 
</head>
<body>
<p>
<?php
    $t=time();
    echo($t . "<br>\n");
    echo(date("h:i:sa")) . "<br>\n";
?>
</p>
<h1>Glossary</h1>
<p style="font-family: monospace;">
<?php
    $misc = ".-_";
    $strIn = "01abc-a.b_c@d-e&f_g^hA(9";
    $strOut = "";
    $temp = "";
    if ( strlen( $strIn ) > 1 ) {
        $temp = substr( $strIn, 1 );
    }
    if ( ctype_alpha( $strIn[0]) ) {
        $strOut .= $strIn[0];
    }
    else {
        $strOut .= 'X';
    }
    foreach ( str_split( $temp ) as $c ) {
        if ( ctype_alnum( $c ) or str_contains( $misc, $c ) ) {
            $strOut .= $c;
        }
        else{
            $strOut .= '_';
        }   
    }
    echo( $strIn . "<br>" );
    echo( $strOut . "<br>" );
?>
</p>
</body>
</html>
