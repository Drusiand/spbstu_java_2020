package literal_translator.java;
import literal_translator.grammar.GrammarReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {

        if (Validator.isValid(args)) {
//          Manager manager = new Manager(new File(args[0]));
//          System.out.println(manager);


            File file = new File(args[0]);

            MReader reader = new MReader(file);
            MWriter writer = new MWriter(reader);

            try {
                BufferedReader buffer = new BufferedReader(new FileReader(new File(reader.getProperties().get(GrammarReader.input))));
                int slice = 0, lBorder, rBorder;
                char[] tmpCharArray = new char[writer.getBuffer_size()];
                while (tmpCharArray.length != 0) {
                    lBorder = slice * writer.getBuffer_size();
                    rBorder = (slice + 1) * writer.getBuffer_size();
                    tmpCharArray = reader.ReadSlice(lBorder, rBorder, buffer).toCharArray();
                    writer.WriteInFile(Translator.process(tmpCharArray));
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