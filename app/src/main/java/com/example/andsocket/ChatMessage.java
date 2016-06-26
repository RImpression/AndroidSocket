package com.example.andsocket;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by RImpression on 2016/6/11 0011.
 */
public class ChatMessage implements Serializable{
    private static final long serialVersionUID=1L;
    private String userName;
    private String msg;
    private String type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
