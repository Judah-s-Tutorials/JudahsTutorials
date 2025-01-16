<?php
function connect () {
    $host = "localhost";
	$database = "judah614_JGlossary1";
	$user = "judah614_JackStraub";
	$password = "Glossary1@01";
	try {
	    $conn = new mysqli($host, $user, $password, $database);
	}
	catch (mysqli_sql_exception $e) {
	    echo $e . "<br>";
	}

	return $conn;
}

function formatEntry( $row, $see ){
    $seq = $row['SEQ_NUM'];
    $slug = $row['SLUG'];
    $termSlug = $slug . "-term";
    $defSlug = $slug . "-def";
    echo "<dt " . "id=\"" . $termSlug . "\">";
    echo $row['TERM'];
    echo "($seq)" . " ";
    echo "<button onclick=\"hideShow('" . $defSlug . "')\">+/-</button>\n";
    echo "</dt>\n";
    echo "<dd id=\"" . $defSlug . "\">\n";
    echo $row['DESCRIPTION'];
    
    $seeCount=mysqli_num_rows($see);
    if ( $seeCount > 0 ) {
        echo "<p class=\"see-also\">See Also:</p>";
        echo "<ul>\n";
        foreach ($see as $row) {
            $text = $row['SEE'];
            echo "<li>" . $text . "</li>\n";
        }
        echo "</ul>\n";
    }
    
    echo "</dd>\n";
}
?>

