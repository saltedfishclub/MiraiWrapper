package cc.sfclub.polar.mirai.packet.message;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageChain {
    public MessageType type;
    public long id;
    @SerializedName("time")
    public long source_time;
    public String text;
    @SerializedName("groupId")
    public long quote_groupId;
    @SerializedName("senderId")
    public long quote_senderId;
    @SerializedName("origin")
    public ArrayList<MessageChain> quote_origin;
    @SerializedName("target")
    public long at_target;
    @SerializedName("display")
    public String at_display;
    public int faceId;
    public String imageId="";
    @SerializedName("url")
    public String imageUrl;
    public String xml;
    public String json;
    @SerializedName("content")
    public String app_content;
}