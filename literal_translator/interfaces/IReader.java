package literal_translator.interfaces;

import literal_translator.java.ReturningCodes;

import java.io.FileInputStream;

public interface IReader extends IConfigurable, IPipelineStep {
    ReturningCodes setInputStream(FileInputStream fis);
}
