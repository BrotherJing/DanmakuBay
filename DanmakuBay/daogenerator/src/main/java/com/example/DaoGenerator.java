package com.example;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.greendao.dao");

        addWord(schema);

        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static void addWord(Schema schema){
        Entity word = schema.addEntity("Word");
        word.addIdProperty().autoincrement().primaryKey();
        word.addLongProperty("shanbay_id");
        word.addLongProperty("learning_id");
        word.addStringProperty("word");
        word.addStringProperty("pronounce");
        word.addStringProperty("definition");
        word.addStringProperty("audio");
    }
}
