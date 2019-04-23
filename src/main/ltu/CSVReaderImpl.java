package ltu;



import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class CSVReaderImpl {

    private final CSVParser parser;
    private final CSVReader reader;

    CSVReaderImpl(String csvFile) {
        parser =
                new CSVParserBuilder()
                        .withIgnoreQuotations(true)
                        .withSeparator(';')
                        .build();
        reader =
                new CSVReaderBuilder(new StringReader(csvFile))
                        .withSkipLines(1)
                        .withCSVParser(parser)
                        .build();
    }

    public String[] getNextLine() throws IOException {
        String[] line;
        line = reader.readNext();

        if (line != null) {
            return line;
        }
        reader.close();

        throw new IOException("No more lines in CSV file.");
    }

    public List<String[]> getAllLines() {
        List<String[]> allLines = null;

        try {
            allLines = reader.readAll(); //returns a LinkedList
            System.out.printf("\nRead %s lines from the CSV file", reader.getRecordsRead());
            reader.close();

        } catch (IOException e) {
            System.out.println(e);
        }

        return allLines;
    }
}
