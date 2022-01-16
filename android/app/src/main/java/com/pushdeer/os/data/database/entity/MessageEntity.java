package com.pushdeer.os.data.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.pushdeer.os.data.api.data.response.Message;

@Entity(tableName = "message")
public class MessageEntity {
    @PrimaryKey
    public int id;
    public String uid;
    public String text;
    public String desp;
    public String type;
    public String created_at;


    public Message toMessage(){
        Message m = new Message();
        m.id = id;
        m.uid = uid;
        m.created_at = created_at;
        m.desp = desp;
        m.text = text;
        m.type = type;
        return m;
    }
}
