package exception;

public class FileReaderException extends Exception {
    public FileReaderException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
