package literal_translator.interfaces;

import literal_translator.java.ReturningCodes;

public interface IExecutable {
    ReturningCodes execute(byte [] data);
}