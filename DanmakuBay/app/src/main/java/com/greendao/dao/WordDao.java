package com.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.greendao.dao.Word;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WORD".
*/
public class WordDao extends AbstractDao<Word, Long> {

    public static final String TABLENAME = "WORD";

    /**
     * Properties of entity Word.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Shanbay_id = new Property(1, Long.class, "shanbay_id", false, "SHANBAY_ID");
        public final static Property Learning_id = new Property(2, Long.class, "learning_id", false, "LEARNING_ID");
        public final static Property Word = new Property(3, String.class, "word", false, "WORD");
        public final static Property Pronounce = new Property(4, String.class, "pronounce", false, "PRONOUNCE");
        public final static Property Definition = new Property(5, String.class, "definition", false, "DEFINITION");
        public final static Property Audio = new Property(6, String.class, "audio", false, "AUDIO");
        public final static Property Audio_local = new Property(7, String.class, "audio_local", false, "AUDIO_LOCAL");
    };

    private DaoSession daoSession;


    public WordDao(DaoConfig config) {
        super(config);
    }
    
    public WordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SHANBAY_ID\" INTEGER," + // 1: shanbay_id
                "\"LEARNING_ID\" INTEGER," + // 2: learning_id
                "\"WORD\" TEXT," + // 3: word
                "\"PRONOUNCE\" TEXT," + // 4: pronounce
                "\"DEFINITION\" TEXT," + // 5: definition
                "\"AUDIO\" TEXT," + // 6: audio
                "\"AUDIO_LOCAL\" TEXT);"); // 7: audio_local
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WORD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Word entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long shanbay_id = entity.getShanbay_id();
        if (shanbay_id != null) {
            stmt.bindLong(2, shanbay_id);
        }
 
        Long learning_id = entity.getLearning_id();
        if (learning_id != null) {
            stmt.bindLong(3, learning_id);
        }
 
        String word = entity.getWord();
        if (word != null) {
            stmt.bindString(4, word);
        }
 
        String pronounce = entity.getPronounce();
        if (pronounce != null) {
            stmt.bindString(5, pronounce);
        }
 
        String definition = entity.getDefinition();
        if (definition != null) {
            stmt.bindString(6, definition);
        }
 
        String audio = entity.getAudio();
        if (audio != null) {
            stmt.bindString(7, audio);
        }
 
        String audio_local = entity.getAudio_local();
        if (audio_local != null) {
            stmt.bindString(8, audio_local);
        }
    }

    @Override
    protected void attachEntity(Word entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Word readEntity(Cursor cursor, int offset) {
        Word entity = new Word( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // shanbay_id
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // learning_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // word
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // pronounce
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // definition
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // audio
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // audio_local
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Word entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setShanbay_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setLearning_id(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setWord(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPronounce(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDefinition(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAudio(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAudio_local(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Word entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Word entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
