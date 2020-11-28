package literal_translator.java;

import literal_translator.grammar.GrammarReader;
import literal_translator.interfaces.IExecutable;
import literal_translator.interfaces.IWriter;

import java.io.*;

public class MWriter implements IWriter {

    String output;
    int buffer_size;

    @Override
    public ReturningCodes setOutputStream(FileOutputStream fos) {
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

    public MWriter(MReader reader) {
        this.output = reader.getProperties().get(GrammarReader.output);
        this.buffer_size = Integer.parseInt(reader.getProperties().get(GrammarReader.buffer));
    }

    public void WriteInFile(char[] output, File directory) {
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(directory, true));
            buf.write(output);
            buf.close();
        } catch (IOException | NullPointerException exception) {
            System.out.println(exception.getMessage());
        }
    }

    static void RefreshFile(File file) {
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public int getBuffer_size() {
        return this.buffer_size;
    }


}
