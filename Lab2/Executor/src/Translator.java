import ru.spbstu.pipeline.*;

import java.util.logging.Logger;
import java.nio.charset.Charset;
import java.util.Map;

public class Translator implements IExecutor {

    final char[] lat = new char[]{'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'r', 's', 't', 'v', 'y', 'z'};

    final char[] rus = new char[]{'б', 'ц', 'д', 'ф', 'г', 'х', 'ж', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'в', 'й', 'з'};

    Boolean mode;   //True - кодирование, иначе декодирование

    IExecutable producer;

    IExecutable consumer;

    Logger logger;

    public Translator(Logger logger) {
        this.logger = logger;
        logger.info("\nConstructing translator...");
    }

    private RC SemanticParse(Map<String, String> propMap, GrammarExecutor g_executor) {
        if (propMap == null)
            return RC.CODE_INVALID_ARGUMENT;
        int instruction_count = 0;
        for (String prop : propMap.keySet()) {
            instruction_count++;
            if (prop.equals(g_executor.token(0))) {
                if (propMap.get(prop).equals("true"))
                    mode = true;
                else if (propMap.get(prop).equals("false"))
                    mode = false;
                else
                    return RC.CODE_INVALID_ARGUMENT;
            } else {
                instruction_count--;
            }
        }
        if (instruction_count != g_executor.numberTokens())
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        return RC.CODE_SUCCESS;
    }

    private byte[] process(byte[] str) {
        char[] tmpData = new String(str, Charset.forName("cp1251")).toCharArray();
        for (int i = 0; i < str.length; i++) {
            tmpData[i] = charReplace(tmpData[i]);
        }
        return new String(tmpData).getBytes((Charset.forName("cp1251")));
    }

    private char charReplace(Character symbol) {
        Character res = symbol;
        boolean caseFl = Character.isUpperCase(symbol);
        symbol = Character.toLowerCase(symbol);
        for (int i = 0; i < lat.length; i++) {
            if (mode) {
                if (symbol == rus[i]) {
                    res = lat[i];
                    break;
                }
            } else {
                if (symbol == lat[i]) {
                    res = rus[i];
                    break;
                }
            }
        }
        if (caseFl)
            return Character.toUpperCase(res);
        return res;
    }

    @Override
    public RC setConfig(String cfg) {
        if (cfg == null)
            return RC.CODE_INVALID_ARGUMENT;
        return SemanticParse(SyntaxParser.Parse(cfg), new GrammarExecutor());
    }

    @Override
    public RC setConsumer(IExecutable c) {
        if (c == null)
            return RC.CODE_INVALID_ARGUMENT;
        this.consumer = c;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setProducer(IExecutable p) {
        if (p == null)
            return RC.CODE_INVALID_ARGUMENT;
        this.producer = p;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] data) {
        byte[] processedData = process(data);
        logger.info("Processing data...\t" + LoggerMessage.GetLogMessage(this.consumer.execute(processedData)));
        return RC.CODE_SUCCESS;
    }
}

