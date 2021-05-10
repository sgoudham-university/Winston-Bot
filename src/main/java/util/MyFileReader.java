package util;

import exception.FileReaderException;

import java.io.File;
import java.util.List;

public interface MyFileReader {
    List<String> read() throws FileReaderException;

    List<String> readFile(File fileToRead) throws FileReaderException;

    List<String> readFile(String fileToRead) throws FileReaderException;
}
