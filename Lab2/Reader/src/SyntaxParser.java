import ru.spbstu.pipeline.BaseGrammar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SyntaxParser {

    static Map<String, String> Parse(String file_name) {

        final Map<String, String> properties = new HashMap<>();

        try {
            File file = new File(file_name);
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String instruction;
            while ((instruction = buffer.readLine()) != null) {
                String[] pair = instruction.split(BaseGrammar.DELIMITER_ATTR_VAL);
                if (pair.length != 2)
                    continue;
                properties.put(pair[0].trim(), pair[1].trim());
            }
        } catch (NullPointerException | IOException exception) {
            return null;
        }
        return properties;
    }


}
