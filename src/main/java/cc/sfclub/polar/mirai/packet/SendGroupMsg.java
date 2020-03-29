package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.Core;
import cc.sfclub.polar.mirai.Main;
import cc.sfclub.polar.mirai.packet.message.client.CGroupMessage;
import cc.sfclub.polar.mirai.packet.response.Status;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SendGroupMsg extends Packet {
    String sessionKey;
    CGroupMessage msg;

    @Override
    public Request build() {
        return new Request.Builder()
                .url(Main.getConf().Address.concat("sendGroupMessage"))
                .post(RequestBody.create(buildRequestBody(), MediaType.parse("application/json")))
                .build();
    }

    public long send(String sessionKey, CGroupMessage msg) {
        this.sessionKey = sessionKey;
        this.msg = msg;
        String raw = super.send();
        if (raw == null) return 0;
        Status stat = Main.getGson().fromJson(raw, Status.class);
        if (stat.messageId == 3 || stat.messageId == 4) {
            Core.getLogger().info("[QQ] AuthKey invalid! trying to re-connect..");
            Main.load(Main.getConf().authKey);
        }
        return stat.messageId;
    }


    @Override
    public String buildRequestBody() {
        return Main.getGson().toJson(msg);
    }

    @Override
    public String name() {
        return "SendGroupMsg";
    }
}
