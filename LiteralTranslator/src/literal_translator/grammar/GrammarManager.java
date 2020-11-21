package literal_translator.grammar;

public class GrammarManager extends GrammarBase {

    public static final String input = "input_file";

    public static final String output = "output_file";

    public static final String reader_cfg = "reader_cfg";

    public static final String writer_cfg = "writer_cfg";

    public static final String executor_and_cfg = "executor_and_cfg";

    public static final String order = "order";

    public static final String DELIMITER_EXEC_AND_CFG = ",";
    public static final String DELIMITER_EXEC_AND_CFG_PAIR = ";";
    public static final String DELIMITER_ORDER = "->";

    public static String[] property_list = new String[]{input, output, reader_cfg, writer_cfg, executor_and_cfg, order};

}

