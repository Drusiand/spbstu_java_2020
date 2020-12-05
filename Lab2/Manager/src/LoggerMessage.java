import ru.spbstu.pipeline.RC;

public class LoggerMessage {
    public static String GetLogMessage(RC code) {
        switch (code) {
            case CODE_SUCCESS:
                return "SUCCESS!";
            case CODE_FAILED_TO_READ:
                return "ERROR: Failed to read from stream";
            case CODE_FAILED_TO_WRITE:
                return "ERROR: Failed to write in stream";
            case CODE_INVALID_ARGUMENT:
                return "ERROR: Invalid argument";
            case CODE_CONFIG_GRAMMAR_ERROR:
                return "ERROR: Config grammar error";
            case CODE_INVALID_INPUT_STREAM:
                return "ERROR: Invalid input stream";
            case CODE_CONFIG_SEMANTIC_ERROR:
                return "ERROR: Config semantic error";
            case CODE_INVALID_OUTPUT_STREAM:
                return "ERROR: Invalid output stream";
            case CODE_FAILED_PIPELINE_CONSTRUCTION:
                return "ERROR: Failed to construct the pipeline";
        }
        return "";
    }

}
