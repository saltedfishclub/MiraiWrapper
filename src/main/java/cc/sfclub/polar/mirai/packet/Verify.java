package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.mirai.Main;
import cc.sfclub.polar.mirai.packet.response.Status;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Verify extends Packet{
    String sessionKey;

    @Override
    public Request build() {
        return new Request.Builder()
                .url(Main.getConf().Address.concat("verify"))
                .post(RequestBody.create(buildRequestBody(), MediaType.parse("application/json")))
                .build();
    }
    public Type send(String sessionKey){
        this.sessionKey=sessionKey;
        String raw=super.send();
        if(raw==null)return Type.SERVER_ERROR;
        Status stat=Main.getGson().fromJson(raw, Status.class);
        for (Type value : Type.values()) {
            if(value.ordinal()==stat.code){
                return value;
            }
        }
        return Type.SERVER_ERROR;
    }
    @Override
    public String buildRequestBody() {
        VBean b = new VBean();
        b.qq=Main.getConf().QQ;
        b.sessionKey=sessionKey;
        return Main.getGson().toJson(b);
    }

    @Override
    public String name() {
        return "Verify";
    }
}
