package literal_translator.java;
import literal_translator.grammar.GrammarExecutor;
import literal_translator.grammar.GrammarReader;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        if (Validator.isValid(args)) {
//          Manager manager = new Manager(new File(args[0]));
//          System.out.println(manager);
            File file = new File(args[0]);

            MReader reader = new MReader(file);
            File raw_data = new File(reader.getProperties().get(GrammarReader.input));
            Translator encoder = new Translator(new File(reader.getProperties().get(GrammarReader.exec_encode_cfg)));
            File processed_data = new File(encoder.getProperties().get(GrammarExecutor.encoded_data));
            Translator decoder = new Translator(new File(reader.getProperties().get(GrammarReader.exec_decode_cfg)));
            MWriter writer = new MWriter(reader);
            File output_file = new File(writer.output);
            MWriter.RefreshFile(processed_data);
            MWriter.RefreshFile(output_file);
            try {
                BufferedReader buffer_raw = new BufferedReader(new FileReader(raw_data));
                BufferedReader buffer_processed = new BufferedReader(new FileReader(processed_data));
                int slice = 0, lBorder, rBorder;
                char[] tmpCharArray = new char[writer.getBuffer_size()];
                while (tmpCharArray.length != 0) {
                    lBorder = slice * writer.getBuffer_size();
                    rBorder = (slice + 1) * writer.getBuffer_size();
                    tmpCharArray = reader.ReadSlice(lBorder, rBorder, buffer_raw).toCharArray();
                    writer.WriteInFile(encoder.process(tmpCharArray), processed_data);
                    tmpCharArray = reader.ReadSlice(lBorder, rBorder, buffer_processed).toCharArray();
                    writer.WriteInFile(decoder.process(tmpCharArray), output_file);
                    slice++;
                }
            } catch (FileNotFoundException exception) {
                System.out.println(exception.getMessage());
            }
        } else {
            System.out.println("No arguments are chosen");
        }
    }

}