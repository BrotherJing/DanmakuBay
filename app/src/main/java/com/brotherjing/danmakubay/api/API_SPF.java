package com.brotherjing.danmakubay.api;

/**
 * Created by Brotherjing on 2015/8/15.
 */
public interface API_SPF {

    int SPEED_LEVEL_SLOW = 0;
    int SPEED_LEVEL_NORMAL = 1;
    int SPEED_LEVEL_FAST = 2;

    String SPF_TOKEN = "spf_token";
    String ITEM_ACCOUNT = "account";
    String ITEM_ACCESS_TOKEN = "access_token";
    String ITEM_COOKIES = "cookies";
    String ITEM_USERINFO = "userinfo";

    String SPF_SETTING = "spf_setting";
    String ITEM_DANMAKU_SPEED = "speed";
    String ITEM_DISPLAY_AREA = "area";
    String ITEM_SHOW_BG = "show_bg";
    String ITEM_DANMAKU_SPEED_LEVEL = "speed_level";
    String ITEM_TEXT_SIZE = "text_size";
    String ITEM_DANMAKU_HEIGHT = "height";

    String ITEM_FIRST_USE = "first_use";

}
