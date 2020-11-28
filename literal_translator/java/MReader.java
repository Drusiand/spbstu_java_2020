package literal_translator.java;

import literal_translator.grammar.GrammarReader;
import literal_translator.interfaces.IExecutable;
import literal_translator.interfaces.IReader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MReader implements IReader {

    private Map<String, String> properties;

    @Override
    public ReturningCodes setInputStream(FileInputStream fis) {
        return null;
    }

    @Override
    public ReturningCodes setConfig(String cfg) {
        return null;
    }

    @Override
    public ReturningCodes setConsumer(IExecutable c) {
        return null;
    }

    @Override
    public ReturningCodes setProducer(IExecutable p) {
        return null;
    }

    @Override
    public ReturningCodes execute(byte[] data) {
        return null;
    }

    public MReader(File file) {
        properties = SyntaxParser.Parse(file);
        if (!isValid())
            System.out.println("Current config does not contain essential properties");
    }

    public String ReadSlice(int lBorder, int rBorder, BufferedReader buffer) {
        StringBuilder tmpStr = new StringBuilder();
        try {
            int tmpChar;
            for (int charPos = 0; charPos < rBorder - lBorder; charPos++) {
                tmpChar = buffer.read();
                if (tmpChar == -1) { //Сигнал конца файла
                    break;
                }
                tmpStr.append((char) tmpChar);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return tmpStr.toString();
    }

    private boolean isValid() {
        for (int prop_iterator = 0; prop_iterator < GrammarReader.property_list.length; prop_iterator += 1) {
            if (!properties.containsKey(GrammarReader.property_list[prop_iterator]))
                return false;
        }
        return true;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
