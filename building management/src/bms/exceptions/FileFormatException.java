package bms.exceptions;

/**
 *Exception thrown when a save file containing a list of building data
 *is invalid.
 */
public class FileFormatException extends Exception {
    /**
     *Constructs a FileFormatException
     */
    public FileFormatException(){
        super();

    }

    /**
     *Constructs a FileFormatException that contains a helpful message
     * detailing why the exception occurred.
     * @param message
     */
    public FileFormatException(String message){
        super(message);

    }

    /**
     *ontains a helpful message detailing why the exception occurred,
     *and an underlying cause of the exception.
     * @param message
     * @param cause
     */
    public FileFormatException(String message,
                                Throwable cause){
        super(message,cause);
    }

}
