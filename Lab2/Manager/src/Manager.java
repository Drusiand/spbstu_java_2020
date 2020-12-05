import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import ru.spbstu.pipeline.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Logger;

public class Manager {

    private FileInputStream inputStream;

    private FileOutputStream outputStream;

    private IPipelineStep[] pipeline;

    private boolean configured = false;

    Logger logger;

    public boolean isConfigured() {
        return configured;
    }

    Manager(String cfg, Logger logger) {
        GrammarManager g_manager = new GrammarManager();
        this.logger = logger;
        logger.info("Constructing pipeline manager...");
        if (SemanticParse(SyntaxParser.Parse(cfg), g_manager) != RC.CODE_SUCCESS)
            logger.info("SUCCESS!");
    }

    RC SemanticParse(Map<String, String> propMap, GrammarManager g_manager) {
        if (propMap == null)
            return RC.CODE_FAILED_TO_READ;
        int instruction_count = 0;
        for (String prop : propMap.keySet()) {
            instruction_count++;
            if (prop.equals(g_manager.token(0))) {
                try {
                    inputStream = new FileInputStream(propMap.get(prop));
                } catch (FileNotFoundException | SecurityException exception) {
                    inputStream = null;
                    return RC.CODE_INVALID_INPUT_STREAM;
                }
            } else if (prop.equals(g_manager.token(1))) {
                try {
                    outputStream = new FileOutputStream(propMap.get(prop));
                } catch (FileNotFoundException | SecurityException exception) {
                    outputStream = null;
                    return RC.CODE_INVALID_OUTPUT_STREAM;
                }
            } else if (prop.equals(g_manager.token(2))) {
                String[] order = propMap.get(prop).trim().split(g_manager.DELIMITER_ORDER);
                pipeline = BuildPipeline(order, g_manager);
                if (pipeline == null) {
                    return RC.CODE_FAILED_PIPELINE_CONSTRUCTION;
                }
            } else {
                instruction_count--;
            }
        }
        if (instruction_count != g_manager.numberTokens())
            return RC.CODE_CONFIG_SEMANTIC_ERROR;
        configured = true;
        return RC.CODE_SUCCESS;
    }

    IPipelineStep[] BuildPipeline(String[] order, GrammarManager g_manager) {
        String logMsg;
        IPipelineStep[] pipeline_tokens = new IPipelineStep[order.length];
        for (int i = 0; i < order.length; ++i) {
            String[] exec_n_config = order[i].trim().split(g_manager.DELIMITER_EXEC_AND_CFG);
            if (exec_n_config.length != 2)
                return null;
            try {
                Class<?> cl = Class.forName(exec_n_config[0].trim());
                // Насколько я помню, на потоке существует договоренность, что конструктор принимает только класс LoggerMessage
                pipeline_tokens[i] = (IPipelineStep) cl.getConstructor(Logger.class).newInstance(logger);

                logMsg = LoggerMessage.GetLogMessage(((IConfigurable) pipeline_tokens[i]).setConfig(exec_n_config[1].trim()));
                logger.info("Configuring...\t" + logMsg);
                if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS))) {
                    return null;
                }

                if (i == 0) {

                    logMsg = LoggerMessage.GetLogMessage(((IReader) pipeline_tokens[i]).setInputStream(this.inputStream));
                    logger.info("Setting input stream...\t" + logMsg);
                    if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                        return null;

                    logMsg = LoggerMessage.GetLogMessage(pipeline_tokens[i].setProducer(null));
                    logger.info("Setting producer...\t" + logMsg);
                    if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                        return null;

                    continue;

                } else if (i == order.length - 1) {

                    logMsg = LoggerMessage.GetLogMessage(((IWriter) pipeline_tokens[i]).setOutputStream(this.outputStream));
                    logger.info("Setting output stream...\t" + logMsg);
                    if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                        return null;

                    logMsg = LoggerMessage.GetLogMessage(pipeline_tokens[i].setProducer(pipeline_tokens[i - 1]));
                    logger.info("Setting producer...\t" + logMsg);
                    if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                        return null;

                } else {

                    logMsg = LoggerMessage.GetLogMessage(pipeline_tokens[i].setProducer(pipeline_tokens[i - 1]));
                    logger.info("Setting producer..." + logMsg);
                    if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                        return null;
                }

                logMsg = LoggerMessage.GetLogMessage(pipeline_tokens[i - 1].setConsumer(pipeline_tokens[i]));
                logger.info("Setting consumer for previous token..." + logMsg);
                if (!logMsg.equals(LoggerMessage.GetLogMessage(RC.CODE_SUCCESS)))
                    return null;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | AssertionError | NullPointerException | InvocationTargetException exception) {
                return null;
            }
        }
        return pipeline_tokens;
    }

    public boolean Run() {
        logger.info("Initializing pipeline execution...");
        RC code = pipeline[0].execute(null);
        if (code == null || code == RC.CODE_SUCCESS) {
            logger.info("Pipeline work complete!");
            return false;
        }
        return true;
    }
}
