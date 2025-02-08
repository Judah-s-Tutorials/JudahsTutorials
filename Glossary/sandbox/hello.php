<?php
    require( "functions.php" );
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
<div id="letter-a">
<p>
<?php
    $str = "xabcd";
    $str = substr_replace( $str, "#", 0, 1 );
    echo $str . "<br>";
    echo "ssss";
?>
<p>
Letter A
</p>
<p id="access_modifier-term" style="margin-left:2em;">
line 01<br>
line 02<br>
line 03<br>
line 04<br>
line 05<br>
line 06<br>
line 07<br>
line 08<br>
line 09<br>
line 10<br>
</p>
</div>
<p>
snowy 01<br>
snowy 02<br>
snowy 03<br>
snowy 04<br>
snowy 05<br>
snowy 06<br>
snowy 07<br>
snowy 08<br>
snowy 09<br>
</p>
<p>
    <a href="javascript:hideShow( 'letter-a')">Hide/Show Letter</a><br>
    <a href="javascript:hideShow( 'access_modifier-term')">Hide/Show Term</a><br>
    <a href="javascript:linkShow( 'access_modifier-term')">Link to Term</a><br>
    <a href="javascript:linkShow( 'access_modifier-term')">Show Term</a><br>
    <button onclick="hideShow( 'access_modifier-term')">Get Letter</button><br>
</p>
</body>
</html>
