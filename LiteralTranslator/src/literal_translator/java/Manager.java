package literal_translator.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import literal_translator.grammar.GrammarManager;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Manager {

    FileInputStream inputStream;
    FileOutputStream outputStream;
    File reader_cfg;
    File writer_cfg;
    Map<String, String> executor_and_cfg;
    Object[] pipeline;

    Manager(File file) {
        SemanticParse(SyntaxParser.Parse(file));
    }

    void SemanticParse(Map<String, String> propMap) {

        for (String prop : propMap.keySet()) {
            switch (prop) {
                case GrammarManager.input:
                    try {
                        inputStream = new FileInputStream(propMap.get(prop));
                    } catch (FileNotFoundException | SecurityException exception) {
                        System.out.println("Chigau desu yo");
                        System.out.println(exception.getMessage());
                    }
                    break;
                case GrammarManager.output:
                    try {
                        outputStream = new FileOutputStream(propMap.get(prop));
                    } catch (FileNotFoundException | SecurityException exception) {
                        System.out.println("Chigau desu yo");
                        System.out.println(exception.getMessage());
                    }
                    break;
                case GrammarManager.reader_cfg:
                    try {
                        reader_cfg = new File(propMap.get(prop));
                    } catch (NullPointerException exception) {
                        System.out.println("Chigau desu yo");
                        System.out.println(exception.getMessage());
                    }
                    break;
                case GrammarManager.writer_cfg:
                    try {
                        writer_cfg = new File(propMap.get(prop));
                    } catch (NullPointerException exception) {
                        System.out.println("Chigau desu yo");
                        System.out.println(exception.getMessage());
                    }
                    break;
                case GrammarManager.executor_and_cfg:
                    executor_and_cfg = EaC_Parse(propMap.get(prop));
                    if (executor_and_cfg == null) {
                        System.out.println("Chigau desu yo");
                        //ошибка
                    }
                    break;
                case GrammarManager.order:
                    String[] order = Order_Parse(propMap.get(prop));
                    if (order != null)
                        pipeline = Build_Pipeline_Order(order);
                    if (pipeline == null) {
                        System.out.println("Chigau desu yo");
                        //ошибка
                    }
                    break;
            }
        }
    }

    Map<String, String> EaC_Parse(String param) {
        Map<String, String> EaC_Map = new HashMap<>();
        String[] collection = param.split(GrammarManager.DELIMITER_EXEC_AND_CFG_PAIR);
        for (String pair : collection) {
            String[] split_pair = pair.split(GrammarManager.DELIMITER_EXEC_AND_CFG);
            if (split_pair.length != 2) {
                continue;
                //ОШИБКА
            }
            EaC_Map.put(split_pair[0].trim(), split_pair[1].trim());
        }
        return EaC_Map;
    }

    Object[] Build_Pipeline_Order(String[] order) {
        Object[] pipeline = new Object[order.length];
        for (int i = 0; i < order.length; ++i) {
            try {
                Class<?> cl = Class.forName(order[i]);
                pipeline[i] = cl.getConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException exception) {
                System.out.println("Cock");
                return null;
            }
        }
        return pipeline;
    }

    String[] Order_Parse(String param) {
        String[] order = param.split(GrammarManager.DELIMITER_ORDER);
        for (String token : order) {
            token = token.trim();
        }
        return order;
    }

}
