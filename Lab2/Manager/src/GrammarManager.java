import ru.spbstu.pipeline.BaseGrammar;

public class GrammarManager extends BaseGrammar {

    public GrammarManager() {
        super(new String[]{"input_file", "output_file", "order"});
    }

    public final String DELIMITER_EXEC_AND_CFG = ",";

    public  final String DELIMITER_ORDER = "->";

}

