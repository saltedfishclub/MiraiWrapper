package cc.sfclub.polar.mirai.packet.message.client;

import cc.sfclub.polar.mirai.packet.message.MessageChain;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CGroupPlain {
    public String sessionKey;
    public long target;
    public ArrayList<MessageChain> messageChain;
}
