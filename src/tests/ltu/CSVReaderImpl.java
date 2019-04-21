package ltu;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class CSVReaderImpl {

    private CSVReader reader = null;

    CSVReaderImpl(String csvFile) {
        //reader = new CSVReader(new FileReader(csvFile));
        reader = new CSVReader(
                new StringReader(csvFile),
                CSVParser.DEFAULT_SEPARATOR,
                CSVParser.DEFAULT_QUOTE_CHARACTER,
                1 // Skip line 1
        );
    }

    public String [] getNextLine() throws IOException {
        String[] line;
        line = reader.readNext();

        if (line != null) {
            return line;
        }
        reader.close();

        throw new IOException("No more lines in CSV file.");
    }
}
