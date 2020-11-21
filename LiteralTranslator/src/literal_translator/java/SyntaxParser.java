package literal_translator.java;

import literal_translator.grammar.GrammarBase;
import literal_translator.grammar.GrammarReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SyntaxParser {

    static Map<String, String> Parse(File file) {

        final Map<String, String> properties = new HashMap<>();

        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String instruction;
            while ((instruction = buffer.readLine()) != null) {
                String[] pair = instruction.split(GrammarBase.DELIMITER_ATTR_VAL);
                if (pair.length != 2)
                    continue;
                properties.put(pair[0].trim(), pair[1].trim());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return properties;
    }


}
