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
<dl>
<?php
    $conn = connect();
    if ($result = $conn -> query("SELECT * FROM DEFINITION")) {
        foreach ($result as $row) {
            $id = $row['ID'];
            $seeAlsoResult = $conn -> query("SELECT * FROM SEE_ALSO WHERE TERM_ID = $id");
            formatEntry( $row, $seeAlsoResult );
            $seeAlsoResult -> free_result();
        }
        $result -> free_result();
    }
?>
</dl>
</body>
</html>
