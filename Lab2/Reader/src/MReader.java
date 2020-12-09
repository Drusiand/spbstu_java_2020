import ru.spbstu.pipeline.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Logger;

public class MReader implements IReader {

    int buffer_size;

    FileInputStream fis;

    IExecutable consumer;

    IExecutable producer;

    Logger logger;

    public MReader(Logger logger) {
        this.logger = logger;
        logger.info("\nConstructing reader...");
    }

    private RC SemanticParse(Map<String, String> propMap, GrammarReader g_reader) {
        if (propMap == null)
            return RC.CODE_INVALID_ARGUMENT;
        int instruction_count = 0;
        for (String prop : propMap.keySet()) {
            instruction_count++;
            if (prop.equals(g_reader.token(0))) {
                try {
                    buffer_size = Integer.parseInt(propMap.get(prop));
                } catch (NumberFormatException exception) {
                    buffer_size = 0;
                    return RC.CODE_INVALID_ARGUMENT;
                }
            } else {
                instruction_count--;
            }
        }
        if (instruction_count != g_reader.numberTokens())
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        return RC.CODE_SUCCESS;
    }

    public byte[] ReadSlice() {
        byte[] data;
        try {
            int maxArraySize;
            if (fis.available() == 0)
                return null;
            else if (fis.available() <= buffer_size)
                maxArraySize = fis.available();
            else
                maxArraySize = buffer_size;
            data = new byte[maxArraySize];
            if (this.fis.read(data, 0, maxArraySize) == -1)
                return null;

        } catch (NullPointerException | IndexOutOfBoundsException | IOException exception) {
            return null;
        }
        return new String(data, Charset.forName("cp1251")).getBytes(Charset.forName("cp1251"));
    }

    @Override
    public RC setInputStream(FileInputStream fis) {
        if (fis == null)
            return RC.CODE_INVALID_INPUT_STREAM;
        this.fis = fis;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String cfg) {
        if (cfg == null)
            return RC.CODE_INVALID_ARGUMENT;
        return SemanticParse(SyntaxParser.Parse(cfg), new GrammarReader());
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
        this.producer = null;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC execute(byte[] data) {
        byte[] newData;
        RC codeMsg = RC.CODE_SUCCESS;
        do {
            newData = ReadSlice();
            if (newData == null)
                break;
            codeMsg = this.consumer.execute(newData);
            logger.info("Reading data...\t" + LoggerMessage.GetLogMessage(codeMsg));
            if (codeMsg != RC.CODE_SUCCESS)
                break;
        } while (true);
        return codeMsg;
    }

}
