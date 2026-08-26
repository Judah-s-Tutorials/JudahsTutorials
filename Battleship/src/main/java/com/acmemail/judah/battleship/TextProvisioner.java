package com.acmemail.judah.battleship;

import static com.acmemail.judah.battleship.StatusMessages.DUP_SHIP_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_BREADTH;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_COL_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_LENGTH;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_P_RECORD;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_ROW_COUNT;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_TYPE;
import static com.acmemail.judah.battleship.StatusMessages.SHIP_TYPE_NOT_FOUND;
import static com.acmemail.judah.battleship.StatusMessages.INVALID_P_COMMAND;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.acmemail.judah.battleship2D.ShipType2D;
import com.acmemail.judah.battleship2D.default_ship_types.Battleship;
import com.acmemail.judah.battleship2D.default_ship_types.Carrier;
import com.acmemail.judah.battleship2D.default_ship_types.Cruiser;
import com.acmemail.judah.battleship2D.default_ship_types.Destroyer;
import com.acmemail.judah.battleship2D.default_ship_types.Submarine;

public class TextProvisioner
{
    private static final CSVFormat  csvFormat  = 
        CSVFormat.DEFAULT.builder()
            .setCommentMarker( '#' )
            .setIgnoreSurroundingSpaces(true)
            .setQuote( '"' )
            .get();
    
    private final List<ShipType2D>  toRegister  = new ArrayList<>();
    private final List<ShipType2D>  toDeploy    = new ArrayList<>();
    private final List<String>      errors      = new ArrayList<>();
    private boolean                 success     = true;
    private Integer                 rows        = null;
    private Integer                 cols        = null;
    
    /**
     * Default constructor.
     */
    private TextProvisioner()
    {
    }
    
    /**
     * Instantiates a new object
     * and initializes it with data from the given path.
     * 
     * @param path  the given path
     * 
     * @throws IOException  if an IOException occurs
     * @throws NullPointerException if path is null
     */
    private TextProvisioner( String path )
        throws IOException
    {
        this( new File( Objects.requireNonNull( path, "path" ) ) );
    }
    
    
    /**
     * Instantiates a new object
     * and initializes it with data from the given file.
     * 
     * @param file  the given file
     * 
     * @throws IOException  if an IOException occurs
     * @throws NullPointerException if file is null
     */
    private TextProvisioner( File file )
        throws IOException
    {
        Objects.requireNonNull( file, "file" );
        List<CSVRecord> recs    = csvRec( file );
        recs.forEach( this::processCSVRec );
    }
        
    /**
     * Instantiates a new object
     * and initializes it with data from the given reader.
     * 
     * @param reader  the given reader
     * 
     * @throws IOException  if an IOException occurs
     * @throws NullPointerException if reader is null
     */
    public TextProvisioner( Reader reader )
        throws IOException
    {
        Objects.requireNonNull( reader, "reader" );
        List<CSVRecord> recs    = csvRec( reader );
        recs.forEach( this::processCSVRec );
    }
    
    /**
     * Instantiate an object of this class
     * and initialize by reading
     * the provisioning records in the given file.
     * 
     * @param path  the given file
     * 
     * @return  the instantiated object
     * 
     * @throws IOException  if an IOException occurs
     * @throws NullPointerException if path is null
     */
    public static TextProvisioner ofFile( String path )
        throws IOException
    {
        Objects.requireNonNull( path, "path" );
        TextProvisioner provisioner = new TextProvisioner( path );
        return provisioner;
    }
    
    /**
     * Instantiates and returns an object of this class.
     * 
     * @return  an object of this class
     */
    public static TextProvisioner of()
    {
        TextProvisioner provisioner = new TextProvisioner();
        return provisioner;
    }
    
    /**
     * Instantiates and returns an object of this class.
     * 
     * @return  an object of this class
     */
    public static TextProvisioner ofReader( Reader reader )
        throws IOException
    {
        TextProvisioner provisioner = new TextProvisioner( reader );
        return provisioner;
    }
    
    /**
     * Parse string into a single provisioning record
     * and process the record.
     * The status of the operation
     * can be determined by invoking the
     * {@link #isSuccess()}
     * and reading the list of errors
     * obtained by invoking {@link #getErrors()}.
     * 
     * @param strRec    the string to parse
     * 
     * @throws NullPointerException if strRec is null
     */
    public void addRec( String strRec )
    {
        Objects.requireNonNull( strRec, "strRec" );
        Deque<String>   errStack    = new ArrayDeque<>();
        try ( CSVParser parser = CSVParser.parse( strRec, csvFormat ); )
        {
            CSVRecord record = parser.getRecords().get( 0 );
            processCSVRec( record );
        }
        catch ( IOException exc )
        {
            String  error   = 
                formatErrorMessage( INVALID_P_RECORD, strRec );
            errStack.push( error );
        }
    }
    
    /**
     * Clears the current list of errors
     * and sets the success status to true.
     */
    public void resetSuccess()
    {
        success = true;
        errors.clear();
    }

    /**
     * Gets an unmodifiable wrapper
     * around the list of types that must be registered.
     * 
     * @return the list of types that must be registered
     */
    public List<ShipType2D> getToRegister()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toRegister );
        return list;
    }

    /**
     * Gets an unmodifiable wrapper
     * around the list of ships that must be deployed.
     * 
     * @return the list of ships that must be deployed
     */
    public List<ShipType2D> getToDeploy()
    {
        List<ShipType2D>    list    = Collections.unmodifiableList( toDeploy );
        return list;
    }

    /**
     * Gets a copy of the current list of errors
     * accumulated in this object.
     * Changes to the current list of errors
     * will not be reflected
     * in this list.
     * The client may reset the current list of errors
     * by invoking {@linkplain #resetSuccess()};
     * 
     * @return a copy of the current list of errors
     * 
     * @see #resetSuccess()
     */
    public List<String> getErrors()
    {
        List<String>    list    = new ArrayList<>( errors );
        return list;
    }

    /**
     * Gets the current status of records
     * processed by this object.
     * The client may reset the status
     * by invoking {@linkplain #resetSuccess()};
     * 
     * @return current status of this object
     * 
     * @see #resetSuccess()
     */
    public boolean isSuccess()
    {
        return success;
    }

    /**
     * Gets the number of grid rows
     * currently provisioned via this object.
     * Null if provisioning of the property
     * has not been processed.
     * 
     * @return number of grid rows provisioned, null if none
     */
    public Integer getRows()
    {
        return rows;
    }


    /**
     * Gets the number of grid columns
     * currently provisioned via this object.
     * Null if provisioning of the property
     * has not been processed.
     * 
     * @return number of grid columns provisioned, null if none
     */
    public Integer getCols()
    {
        return cols;
    }

    /**
     * Extracts and returns a list of CSV records
     * from the strings in the given file.
     * See {@ink #csvRec(Reader)} for additional details.
     * 
     * @param file  the given file
     * 
     * @return  a list of CSV records extracted from the given file
     */
    private List<CSVRecord> csvRec( File file )
    {
        List<CSVRecord> recs;
        try ( FileReader  fReader = new FileReader( file ); )
        {
            recs = csvRec( fReader );
        }
        catch ( IOException exc )
        {
            success = false;
            recs = new ArrayList<>();
        }
        return recs;
    }


    /**
     * Extracts and returns a list of CSV records
     * from the strings in the given reader.
     * If an IO error occurs,
     * an error message is added to the errors list
     * and an empty list is returned.
     * <p>
     * Postcondition: if an I/O error occurs
     * a) an error message will added to the error list, and
     * b) the status of this object will be set to false
     * 
     * @param reader  the given reader
     * 
     * @return  
     *      a list of CSV records extracted from the given reader,
     *      or an empty list if an IOException occurs
     */
    private List<CSVRecord> csvRec( Reader reader )
    {
        List<CSVRecord> recs;
        try (
            CSVParser parser = csvFormat.parse( reader );
        )
        {
            recs = parser.getRecords();
        }
        catch ( IOException exc )
        {
            success = false;
            recs = new ArrayList<>();
        }
        return recs;
    }
    
    /**
     * Process a command parsed into a CSV record.
     * 
     * @param rec   the record containing the command to process
     */
    private void processCSVRec( CSVRecord rec )
    {
        String  command = rec.get( 0 ).toUpperCase();
        switch ( command )
        {
        case "DIM" -> dim( rec );
        case "TYPE" -> type( rec );
        case "DEPLOY" -> deploy( rec );
        default -> invalidRec( rec );
        }
    }
    
    /**
     * Assemble diagnostic messages for CSV records
     * that do not contain recognized commands.
     * 
     * @param rec   the CSV record in question
     */
    private void invalidRec( CSVRecord rec )
    {
        Deque<String>   errStack    = new ArrayDeque<>();
        String          errMessage  =
            formatErrorMessage( INVALID_P_COMMAND, rec.get( 1 ) );
        errStack.push( errMessage );
        processErrStack( rec, errStack );
    }
    
    /**
     * Process a CSV record containing a DIM command
     * (a record that describes the dimensions of the grid).
     * <p>
     * Postcondition: 
     * if processing is successful
     * the rows and cols properties will be initialized
     * 
     * @param rec   the CSV record to process
     */
    private void dim( CSVRecord rec )
    {
        Deque<String>   errStack    = new ArrayDeque<>();
        if ( rec.size() != 3 )
        {
            success = false;
        }
        else
        {
            String  strRows     = rec.get( 1 );
            int     testRows    = 
                getPositiveInt( strRows );
            
            String  strCols     = rec.get( 2 );
            int     testCols    = 
                getPositiveInt( strCols );

            if ( testRows < 1 )
            {
                String  errMessage  = 
                    formatErrorMessage( INVALID_ROW_COUNT, strRows );
                errStack.push( errMessage );
            }
            else
                rows = testRows;
            
            if ( testCols < 1 )
            {
                String  errMessage  = 
                    formatErrorMessage( INVALID_COL_COUNT, strCols );
                errStack.push( errMessage );
            }
        }
        
        processErrStack( rec, errStack );
    }
    
    /**
     * Process a CSV record containing a TYPE command
     * (a command describing the type of a ship;
     * see {@link ShipType2D}.
     * <p>
     * <ol>
     * Postcondition:
     * <ol>
     * <li>
     * If the record contains two values,
     * and the second contains the token "default,"
     * all the default ship types
     * are added to the list of ship types to be registered.<\
     * </li>
     * <li>
     * If the record contains four values,
     * and processing completes successfully,
     * a single ShipType2D object is instantiated
     * and added to the list of ships types to be registered.
     * A ship type with the given name
     * must no already be in the list.
     * </li>
     * </ol>
     * 
     * @param rec
     */
    private void type( CSVRecord rec )
    {
        Deque<String>   errStack    = new ArrayDeque<>();
        int     valCount    = rec.size();
        if ( valCount == 2 )
        {
            String  type    = rec.get( 1 ).toUpperCase();
            if ( !type.contains( "DEFAULT" ) )
            {
                errStack.push( formatErrorMessage( INVALID_TYPE, type ) );
            }
            else
            {
                toRegister.add( Battleship.getType() );
                toRegister.add( Carrier.getType() );
                toRegister.add( Cruiser.getType() );
                toRegister.add( Destroyer.getType() );
                toRegister.add( Submarine.getType() );
            }
        }
        else if ( valCount == 4 )
        {
            String  type        = rec.get( 1 );
            String  strLen      = rec.get( 2 );
            String  strBreadth  = rec.get( 3 );
            
            int     intLength   = getPositiveInt( strLen );
            int     intBreadth  = getPositiveInt( strBreadth );
            
            if ( intLength < 1 )
                errStack.push( formatErrorMessage( INVALID_LENGTH, strLen ) );
            if ( intBreadth < 1 )
                errStack.push( formatErrorMessage( INVALID_BREADTH, strBreadth ) );
            
            if ( errStack.isEmpty() )
            {
                ShipType2D  shipType    = 
                    new ShipType2D( type, intLength, intBreadth, null );
                if ( getShipType( shipType ) != null )
                    errStack.push( formatErrorMessage( DUP_SHIP_TYPE, strLen ) );
                else
                    toRegister.add( shipType );
            }
        }
        else
        {
            String  invType = formatErrorMessage( INVALID_TYPE, rec.get( 0 ) );
            errStack.push( invType );
        }
        processErrStack( rec, errStack );
    }
    
    /**
     * Process a CSV record containing a DEPLOY command
     * (a command describing the type of a ship to be deployed).
     * The type of ship to be deployed
     * must reside in the list of types to be registered.
     * <p>
     * Postcondition:
     * If the processing completes successfully,
     * a Ship2D object is instantiated
     * and added to the list of ships to be deployed.
     * 
     * @param rec   the CSV record to process
     */
    private void deploy( CSVRecord rec )
    {
        Deque<String>   errStack    = new ArrayDeque<>();
        int     valCount    = rec.size();
        if ( valCount != 2 )
        {
            String  errCount    = "field count = " + valCount;
            String  message     = formatErrorMessage( INVALID_TYPE, errCount );
            errStack.push( message );
        }
        else
        {
            String      name    = rec.get( 1 );
            ShipType2D  type    = getShipType( name );
            if ( type == null )
            {
                String  err = formatErrorMessage( SHIP_TYPE_NOT_FOUND, name );
                errStack.push( err );
            }
            else
                toDeploy.add( type );
        }
        processErrStack( rec, errStack );
    }
    
    /**
     * Parse a string containing an integer &gte; 1.
     * If successful, the parsed integer is returned,
     * otherwise -1 is returned.
     *  
     * @param strInt    the string to parse
     * 
     * @return  the parsed integer, or -1 if parsing failed
     */
    private int getPositiveInt( String strInt )
    {
        int     result  = -1;
        try
        {
            result = Integer.parseInt( strInt );
            if ( result < 1 )
                throw new NumberFormatException( "not a positive integer" );
        }
        catch ( NumberFormatException exc )
        {
            result = -1;
        }
        return result;
    }
    
    /**
     * Format a given message and value into an error message.
     * The result will begin with the given message,
     * and terminate with the given value enclosed in 
     * square brackets ([]).
     * 
     * @param message   the given message
     * @param val       the given value
     * 
     * @return  the formatted error message
     */
    private static String formatErrorMessage( String message, String val )
    {
        StringBuilder   bldr    = 
            new StringBuilder( message );
        bldr.append( " [" ).append( val ).append( "]" );
        return bldr.toString();
    }
    
    /**
     * Given a non-empty stack of error messages
     * and an associated record,
     * add to the list of error messages
     * each message on the stack (in "pop" order)
     * suffixed with the values contained in the record.
     * If the stack is empty it is ignored.
     * <p>
     * Postcondition:
     * if the stack is non-empty,
     * messages are added to the list of error messages
     * as described above,
     * and the success property of this object
     * is set to false.
     * 
     * @param rec   the give CSV record
     * @param stack the given stack
     */
    private void processErrStack( CSVRecord rec, Deque<String> stack )
    {
        if ( !stack.isEmpty() )
        {
            success = false;
            errors.add( invalidRecMessage( rec ) );
            while ( !stack.isEmpty() )
                errors.add( stack.pop() );
        }
    }
    
    /**
     * Given a CSV record,
     * format an error message indicating
     * that the record is invalid.
     * Append to the record a string describing the record's values.
     * 
     * @param rec   the given CSV record
     * 
     * @return  a formatted error message as described above
     */
    private static String invalidRecMessage( CSVRecord rec )
    {
        StringBuilder   bldr    = 
            new StringBuilder( INVALID_P_RECORD );
        bldr.append( " [" ).append( toString( rec ) ).append( "]" );
        return bldr.toString();
    }
    
    /**
     * Format a string containing the values
     * from a CSV record
     * separated by commas.
     * 
     * @param rec   the given CSV record
     * 
     * @return  the string formatted as described above
     */
    private static String toString( CSVRecord rec )
    {
        StringBuilder   bldr    = new StringBuilder();
        for ( String value : rec.values() )
            bldr.append( value ).append( "," );
        bldr.deleteCharAt( bldr.length() - 1 );
        return bldr.toString();
    }
    
    /**
     * Given a ShipType2D object,
     * get from the list of ship types to be registered,
     * the object with the same name 
     * as the given object.
     * Null is returned if not found.
     * 
     * @param shipType  the given ship type
     * 
     * @return  the ShipType2D object, or null if not found
     */
    private ShipType2D getShipType( ShipType2D shipType )
    {
        ShipType2D  type    = getShipType( shipType.typeName() );
        return type;
    }
    
    /**
     * Obtain from the list of ship types to  be registered,
     * the object with the given name. 
     * Null is returned if not found.
     * 
     * @param shipType  the given ship type
     * 
     * @return  the ShipType2D object, or null if not found
     */
    private ShipType2D getShipType( String name )
    {
        ShipType2D  type    = 
            toRegister.stream()
                .filter( t -> name.equals( t.typeName() ) )
                .findFirst().orElse( null );
        return type;
    }
}
