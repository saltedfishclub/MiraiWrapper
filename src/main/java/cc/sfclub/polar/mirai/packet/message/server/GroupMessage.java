package cc.sfclub.polar.mirai.packet.message.server;

import cc.sfclub.polar.mirai.packet.message.MessageChain;
import cc.sfclub.polar.mirai.packet.message.Sender;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GroupMessage {
    public String type;
    public ArrayList<MessageChain> messageChain;
    public Sender sender;
}
