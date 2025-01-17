<?php require( "functions.php" ); ?>

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
<?php
    connect();
    traverseAlphabet();
?>
</body>
</html>
