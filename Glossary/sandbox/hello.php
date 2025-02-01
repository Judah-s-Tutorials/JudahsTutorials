<?
    // php require( "functions.php" );
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ad Hoc</title>
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
<p>
<?php
$str = getGlossaryRef( "#class-2", "#class-2" );
echo $str . "<br>";
echo "<a href=\"#dupe\">Testing</a> <br>";
function getGlossaryRef( $term, $termSlug ) {
    $href = "<a href=\""
    . $termSlug
    . "\">"
        .$term
        . "</a>";
    return $href;
}
?>
</p>
<p>
<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
</p>
<div id="dupe">
<p>
"Jump to here"
</p>
</div>
</body>
</html>
