package com.brotherjing.danmakubay.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.brotherjing.danmakubay.utils.beans.SentenceBean;
import com.greendao.dao.DaoMaster;
import com.greendao.dao.DaoSession;
import com.greendao.dao.Sentence;
import com.greendao.dao.SentenceDao;
import com.greendao.dao.Word;
import com.greendao.dao.WordDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by Brotherjing on 2015/8/23.
 */
public class WordDBManager {

    private Context context;
    SQLiteDatabase db_write;
    DaoMaster daoMaster;
    DaoSession daoSession;
    WordDao wordDao;
    SentenceDao sentenceDao;

    public WordDBManager(Context context){
        this.context = context;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,"word_db",null);
        db_write = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db_write);
        daoSession = daoMaster.newSession();
        wordDao = daoSession.getWordDao();
        sentenceDao = daoSession.getSentenceDao();
    }

    public boolean addWord(Word word){
        if(ifExist(word))return false;
        wordDao.insert(word);
        return true;
    }

    public void addSentences(List<SentenceBean> list,Word word){
        for(SentenceBean sb:list){
            sentenceDao.insert(from(sb, word.getShanbay_id()));
        }
    }

    public Sentence from(SentenceBean sb,long id){
        Sentence sentence = new Sentence();
        sentence.setShanbay_id(id);
        sentence.setSentence(TextUtil.removeTags(sb.getSentence()));
        sentence.setTranslation(sb.getTranslation());
        return sentence;
    }

    public boolean ifExist(Word word){
        Long shanbay_id = word.getShanbay_id();
        return wordDao.queryBuilder().where(WordDao.Properties.Shanbay_id.eq(shanbay_id)).count()>0;
    }

    public List<Word> getList(){
        return wordDao.queryBuilder().build().forCurrentThread().list();
    }

    public void deleteSentence(Sentence sentence){
        sentenceDao.delete(sentence);
    }

    public void deleteWord(Word word){
        for(Sentence sentence:word.getSentenceList()){
            deleteSentence(sentence);
        }
        wordDao.delete(word);
    }

}
