package com.brotherjing.danmakubay.utils.providers;

import com.brotherjing.danmakubay.utils.beans.WordBean;
import com.greendao.dao.Word;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public class ShanbayProvider {

    public Word from(WordBean wordBean){
        Word word = new Word();
        word.setWord(wordBean.getContent());
        word.setShanbay_id(wordBean.getId());
        word.setDefinition(wordBean.getDefinition());
        word.setPronounce(wordBean.getPronunciation());
        word.setAudio(wordBean.getAudioUrl());
        return word;
    }

}
