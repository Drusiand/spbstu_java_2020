package literal_translator.java;

import literal_translator.grammar.GrammarExecutor;
import literal_translator.grammar.GrammarReader;
import literal_translator.interfaces.IExecutable;
import literal_translator.interfaces.IExecutor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Translator implements IExecutor {
    final static char[] lat = new char[]{'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'y', 'z'};
    final static char[] rus = new char[]{'б', 'ц', 'д', 'ф', 'г', 'х', 'ж', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'в', 'й', 'з'};

    Boolean mode;   //True - кодирование, иначе декодирование
    Map<String, String> properties;

    public Translator(File cfg) {
        properties = SyntaxParser.Parse(cfg);
        for (String key : properties.keySet()) {
            if (key.equals(GrammarExecutor.mode))
                this.mode = Boolean.parseBoolean(properties.get(key));
        }
    }

    public char[] process(char[] str) {
        for (int i = 0; i < str.length; i++)
            str[i] = charReplace(str[i]);
        return str;
    }

    private char charReplace(Character symbol) {
        Character res = symbol;
        boolean caseFl = Character.isUpperCase(symbol);
        symbol = Character.toLowerCase(symbol);
        for (int i = 0; i < lat.length; i++) {
            if (mode) {
//                if (symbol == lat[i]) {
//                    res = rus[i];
//                    break;
//                }
                if (symbol == rus[i]) {
                    res = lat[i];
                    break;
                }
            } else {
                if (symbol == lat[i]) {
                    res = rus[i];
                    break;
                }
//                if (symbol == rus[i]) {
//                    res = lat[i];
//                    break;
//                }
            }
        }
        if (caseFl)
            return Character.toUpperCase(res);
        return res;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public ReturningCodes setConfig(String cfg) {
        return null;
    }

    @Override
    public ReturningCodes setConsumer(IExecutable c) {
        return null;
    }

    @Override
    public ReturningCodes setProducer(IExecutable p) {
        return null;
    }

    @Override
    public ReturningCodes execute(byte[] data) {
        return null;
    }
}

