package cc.sfclub.polar.mirai;

import cc.sfclub.polar.Core;
import cc.sfclub.polar.events.messages.TextMessage;
import cc.sfclub.polar.mirai.packet.message.MessageChain;
import cc.sfclub.polar.mirai.packet.message.MessageType;
import cc.sfclub.polar.mirai.packet.message.client.CGroupMessage;
import cc.sfclub.polar.mirai.packet.sendGroupMsg;
import org.nutz.repo.Base64;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Bot implements cc.sfclub.polar.wrapper.Bot {
    @Override
    public String getPlatfrom() {
        return "QQ";
    }

    @Override
    public long sendMessage(long gid, String message) {
        CGroupMessage msga=new CGroupMessage();
        ArrayList<MessageChain> msg=new ArrayList<>();
        MessageChain c=new MessageChain();
        c.setType(MessageType.Plain);
        c.setText(message);
        msg.add(c);
        msga.setMessageChain(msg);
        msga.setTarget((int) gid);
        msga.setSessionKey(Main.getSession());
        return new sendGroupMsg().send(Main.getSession(),gid,msga);
    }

    @Override
    public void deleteMsg(long l) {

    }

    @Override
    public long sendMessage(TextMessage textMessage, String s) {
        CGroupMessage msg=new CGroupMessage();
        msg.setQuote(textMessage.getMsgID());
        msg.setSessionKey(Main.getSession());
        ArrayList<MessageChain> msgs=new ArrayList<>();
        String[] sp = spilt(s);
        for (String s1 : sp) {
            MessageChain mc=new MessageChain();
            s1=s1.replaceAll("\\\\\\[","[").replaceAll("\\\\\\]","]");
            if(s1.startsWith("[")){
                s1=s1.replaceFirst("\\[","");
                s1=s1.substring(0,s1.length()-1);
                if(!s1.contains(",")){
                    String[] s2=s1.split(":");
                    if(s2.length==0){
                        mc.setType(MessageType.AtAll);
                        msgs.add(mc);
                        continue;
                    }
                    switch (MessageType.valueOf(s2[0])){
                        case At:
                            mc.setType(MessageType.At);
                            mc.setAt_target(Long.parseLong(s2[1]));
                            break;
                        case Face:
                            mc.setType(MessageType.Face);
                            mc.setFaceId(Integer.parseInt(s2[1]));
                            break;
                        case Image:
                            mc.setType(MessageType.Image);
                            if(s2[1].endsWith(".jpg") || s2[1].endsWith(".png")){
                                mc.setImageId(s2[1]);
                            }else{
                                mc.setImageUrl(Base64.URLSafe.decode(s2[1]));
                            }
                            break;
                        case Xml:
                            mc.setType(MessageType.Xml);
                            mc.setXml(s2[1]);
                            break;
                        case Json:
                            mc.setType(MessageType.Json);
                            mc.setJson(s2[1]);
                            break;
                        case App:
                            mc.setType(MessageType.App);
                            mc.setApp_content(s2[1]);
                            break;
                        case AtAll:
                            mc.setType(MessageType.AtAll);
                            break;
                        default:
                            mc.setType(MessageType.Plain);
                            mc.setText("%INVAILD_"+s2[0]+"%");
                            break;
                    }
                }
            }else{
                mc.setType(MessageType.Plain);
                mc.setText(s1);
            }
            msgs.add(mc);
        }
        msg.setMessageChain(msgs);
        msg.setTarget(textMessage.getGroupID());
        return new sendGroupMsg().send(Main.getSession(),textMessage.getGroupID(),msg);
    }

    @Override
    public long sendMessage(TextMessage textMessage, String[] strings) {
        StringBuilder str = new StringBuilder();
        for (String string : strings) {
            str.append(string).append("\n");
        }
        return sendMessage(textMessage, str.toString());
    }

    @Override
    public Byte[] getImage(String s) {
        return new Byte[0];
    }

    public static String[] spilt(String str)
    {
        char[] ch = str.toCharArray();
        List<String> list = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<ch.length; i++)
        {
            char c = ch[i];
            area_1:if (c == '[')
            {
                if (i > 0 && ch[i-1] == '\\') break area_1;
                if (builder.length() > 0) list.add(builder.toString());
                builder = new StringBuilder();
                builder.append(c);
                continue;
            }
            else if (c == ']')
            {
                if (i > 0 && ch[i-1] == '\\') break area_1;
                builder.append(c);
                list.add(builder.toString());
                builder = new StringBuilder();
                continue;
            }
            builder.append(c);
        }
        if(builder.length() > 0) list.add(builder.toString());
        String[] strs = new String[list.size()];
        for (int i = 0; i < strs.length; i++)
        {
            strs[i] = list.get(i);
        }
        return strs;
    }
}
