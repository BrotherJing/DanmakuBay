package com.example;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
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
        Property shanbayId = word.addLongProperty("shanbay_id").getProperty();
        word.addLongProperty("learning_id");
        word.addStringProperty("word");
        word.addStringProperty("pronounce");
        word.addStringProperty("definition");
        word.addStringProperty("audio");
        word.addStringProperty("audio_local");

        Entity sentence = schema.addEntity("Sentence");
        sentence.addIdProperty().autoincrement().primaryKey();
        sentence.addStringProperty("sentence");
        sentence.addStringProperty("translation");
        Property wordId = sentence.addLongProperty("shanbay_id").getProperty();

        word.addToMany(shanbayId,sentence,wordId);
    }
}
