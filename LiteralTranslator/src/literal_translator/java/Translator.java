package literal_translator.java;

import literal_translator.interfaces.IExecutable;
import literal_translator.interfaces.IExecutor;

public class Translator implements IExecutor {

    final static char[] lat = new char[]{'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'y', 'z'};
    final static char[] rus = new char[]{'б', 'ц', 'д', 'ф', 'г', 'х', 'ж', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'в', 'й', 'з'};

    public static char[] process(char[] str) {
        for (int i = 0; i < str.length; i++)
            str[i] = charReplace(str[i]);
        return str;
    }

    private static char charReplace(Character symbol) {
        Character res = symbol;
        boolean caseFl = Character.isUpperCase(symbol);
        symbol = Character.toLowerCase(symbol);
        for (int i = 0; i < lat.length; i++) {
            if (symbol == lat[i]) {
                res = rus[i];
                break;
            } else if (symbol == rus[i]) {
                res = lat[i];
                break;
            }
        }
        if (caseFl)
            return Character.toUpperCase(res);
        return res;
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

