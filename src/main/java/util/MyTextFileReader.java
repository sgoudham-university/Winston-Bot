package util;

import exception.FileReaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.WINSTON_VOICE_LINES;

/**
 * MyTextFileReader reads from a given file
 */
public class MyTextFileReader implements MyFileReader {

    private String filePath;

    public MyTextFileReader() {
        filePath = WINSTON_VOICE_LINES.toString();
    }

    @Override
    public List<String> read() throws FileReaderException {
        return readFromFile(new File(filePath));
    }

    @Override
    public List<String> readFile(File fileToRead) throws FileReaderException {
        return readFromFile(fileToRead);
    }

    @Override
    public List<String> readFile(String fileToRead) throws FileReaderException {
        return readFromFile(new File(fileToRead));
    }

    private List<String> readFromFile(File fileToRead) throws FileReaderException {
        List<String> fileLines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead))) {
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                fileLines.add(line);
            }
        } catch (IOException ioe) {
            throw new FileReaderException(ioe.getMessage(), ioe);
        }

        return fileLines;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
