package cc.sfclub.polar.mirai.packet.message.client;

import cc.sfclub.polar.mirai.packet.message.MessageChain;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CGroupMessage {
    public String sessionKey;
    public long target;
    public ArrayList<MessageChain> messageChain;
    public long quote;
}
