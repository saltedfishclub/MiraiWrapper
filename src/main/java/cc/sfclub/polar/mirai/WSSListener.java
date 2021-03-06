package cc.sfclub.polar.mirai;

import cc.sfclub.polar.Core;
import cc.sfclub.polar.events.messages.TextMessage;
import cc.sfclub.polar.mirai.packet.message.MessageChain;
import cc.sfclub.polar.mirai.packet.message.MessageType;
import cc.sfclub.polar.mirai.packet.message.server.GroupMessage;
import lombok.SneakyThrows;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nutz.repo.Base64;

public class WSSListener extends WebSocketListener {
    Main main;

    public WSSListener(Main main) {
        super();
        this.main = main;
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        if (code != 1000) {
            Core.getLogger().info("[QQ] WSConnection Closing!! Code:{},Reason:{}", code, reason);
        }
    }

    @SneakyThrows
    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        Core.getLogger().error("[QQ] Failed to establish connection.Exception:{},Resp: {}: {}", t.getMessage(), response.code(), response.body().string());
        main.load(Main.getConf().authKey);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        Core.getLogger().info("[QQ] Connecting...");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        GroupMessage gm=Main.getGson().fromJson(text,GroupMessage.class);
        StringBuilder fin=new StringBuilder();
        gm.messageChain.forEach(c -> {
            if (c.getType() != MessageType.Source) {
                fin.append(ChainToText(c));
            }
        });
        Core.getInstance().getMessage().post(new TextMessage("QQ", gm.messageChain.get(0).id, gm.sender.id, fin.toString(), gm.sender.group.id));
        if(Main.getConf().displayMessage){
            Core.getLogger().info("[QQ][Group]{} ~ {}({}): {}",gm.sender.group,gm.sender.memberName,gm.sender.id,fin.toString());
        }
    }
    public String ChainToText(MessageChain mch){
        StringBuilder str=new StringBuilder();
        switch(mch.getType()){
            case Quote:
                str.append("[Quote:")
                        .append(mch.id)
                        .append(",")
                        .append("senderId:")
                        .append(mch.quote_senderId)
                        .append(",groupId:")
                        .append(mch.quote_groupId)
                        .append("]");
                break;
            case At:
                str.append("[At:")
                        .append(mch.at_target)
                        .append(",display:")
                        .append(mch.at_display.replaceAll("\\[","\\\\[").replaceAll("\\]","\\\\]"))
                        .append("]");
                break;
            case AtAll:
                str.append("[AtAll]");
                break;
            case Face:
                str.append("[Face:")
                        .append(mch.faceId)
                        .append("]");
                break;
            case Plain:
                str.append(mch.text.replaceAll("\\[","\\\\[").replaceAll("\\]","\\\\]"));
                break;
            case Image:
                str.append("[Image:")
                        .append(Base64.URLSafe.encode(mch.imageUrl))
                        .append("]");
                break;
            case Xml:
                str.append("[XML:")
                        .append(mch.xml.replaceAll("\\[","\\\\[").replaceAll("\\]","\\\\]"))
                        .append("]");
                break;
            case Json:
                str.append("[JSON:")
                        .append(mch.json)
                        .append("]");
                break;
            case App:
                str.append("[App:")
                        .append(mch.app_content)
                        .append("]");
                break;
        }
        return str.toString();
    }
}
