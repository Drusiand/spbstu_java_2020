package literal_translator.interfaces;

import literal_translator.java.ReturningCodes;

import java.io.FileOutputStream;

public interface IWriter extends IConfigurable, IPipelineStep {
    ReturningCodes setOutputStream(FileOutputStream fos);
}