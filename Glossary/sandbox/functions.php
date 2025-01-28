<?php
$letterClosureNeeded = false;
$conn;

function getQueryByLetter( $letter ) {
    $query = "SELECT * FROM definition "
        . "WHERE term like \"" . $letter . "%\" " 
        . "order by term, seq_num";
    return $query;
}

function connect () {
    global $conn;
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

function traverseAlphabet()
{
    global $conn;
    foreach( range( 'a', 'z' ) as $l )
    {
        $letterSQL = getQueryByLetter( $l );
        $letterResult = $conn -> query( $letterSQL );
        if ( $letterResult -> num_rows > 0 ) {
            newLetter( $l );
            foreach ($letterResult as $row) {
                $id = $row['id'];
                $seeAlsoResult = $conn -> query("SELECT * FROM see_also WHERE term_id = $id");
                formatEntry( $row, $seeAlsoResult );
                $seeAlsoResult -> free_result();
            }
        }
        closeLetter();
        $letterResult -> free_result();
    }
}

function formatEntry( $row, $see ){
    $seq = $row['seq_num'];
    $slug = $row['slug'];
    $termSlug = $slug . "-term";
    $defSlug = $slug . "-def";
    echo "<dt " . "id=\"" . $termSlug . "\" onclick=\"hideShow('" . $defSlug . "')\">";
    echo $row['term'];
    if ( $seq != 0 )
        echo "($seq)" . " ";
    echo "</dt>\n";
    echo "<dd id=\"" . $defSlug . "\">\n";
    echo $row['description'];
    
    $seeCount=mysqli_num_rows($see);
    if ( $seeCount > 0 ) {
        echo "<p class=\"see-also\">See Also:</p>\n";
        echo "<ul>\n";
        foreach ($see as $row) {
            $text = $row['url'];
            echo "<li>" . $text . "</li>\n";
        }
        echo "</ul>\n";
    }
    
    echo "</dd>\n";
}

function newLetter( $letter ) {
    global $letterClosureNeeded;
    if ( $letterClosureNeeded ) {
        closeLetter();
    }
    $letterClosureNeeded = true;
    $id = "letter-$letter";
    echo "<p class='letter-heading'>\n";
    echo "<button onclick=\"hideShow('" . $id . "')\">+/-</button> " . $letter . "\n";
    echo "</p>\n";
    echo "<div id='$id' class='letter'>\n";
    echo "<dl>\n";
}

function closeLetter() {
    global $letterClosureNeeded;
    if ( $letterClosureNeeded ) {
        echo "</dl>\n";
        echo "</div>\n";
        $letterClosureNeeded = false;
    }
}
?>

