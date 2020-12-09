import ru.spbstu.pipeline.*;
import java.io.*;
import java.util.Map;
import java.util.logging.Logger;

public class MWriter implements IWriter {

    int buffer_size;

    FileOutputStream fos;

    IExecutable producer;

    IExecutable consumer;

    Logger logger;

    public MWriter(Logger logger) {
        this.logger = logger;
        logger.info("\nConstructing writer...");
    }

    private RC SemanticParse(Map<String, String> propMap, GrammarWriter g_writer) {
        if (propMap == null)
            return RC.CODE_INVALID_ARGUMENT;
        int instruction_count = 0;
        for (String prop : propMap.keySet()) {
            instruction_count++;
            if (prop.equals(g_writer.token(0))) {
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
        if (instruction_count != g_writer.numberTokens())
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        return RC.CODE_SUCCESS;
    }

    public RC WriteSlice(byte[] data) {
        if (buffer_size > data.length)
            buffer_size = data.length;
        try {
            int tmp = 0;
            int i;
            //while (tmp <= data.length) {
            for (int j = 0; j <= data.length/buffer_size; ++j) {
                for (i = tmp; i < tmp + buffer_size; ++i){
                    if (i == data.length)
                        break;
                    this.fos.write(data[i]);
                }
                tmp += buffer_size;
            }
        } catch (IOException e) {
            return RC.CODE_FAILED_TO_WRITE;
        }
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setOutputStream(FileOutputStream fos) {
        if (fos == null)
            return RC.CODE_INVALID_OUTPUT_STREAM;
        this.fos = fos;
        return RC.CODE_SUCCESS;
    }

    @Override
    public RC setConfig(String cfg) {
        if (cfg == null)
            return RC.CODE_INVALID_ARGUMENT;
        return SemanticParse(SyntaxParser.Parse(cfg), new GrammarWriter());
    }

    @Override
    public RC setConsumer(IExecutable c) {
        this.consumer = null;
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
        try {
            logger.info("Writing data...\t" + LoggerMessage.GetLogMessage(WriteSlice(data)));
        } catch (NullPointerException exception) {
            logger.info(LoggerMessage.GetLogMessage(RC.CODE_INVALID_OUTPUT_STREAM));
            return null;
        }
        return RC.CODE_SUCCESS;
    }

}
