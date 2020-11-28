package literal_translator.interfaces;

import literal_translator.java.ReturningCodes;

public interface IPipelineStep extends IExecutable {
    ReturningCodes setConsumer(IExecutable c);
    ReturningCodes setProducer(IExecutable p);
}
