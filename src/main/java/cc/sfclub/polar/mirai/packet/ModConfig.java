package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.mirai.Main;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

class MBean{
    String sessionKey;
    boolean enableWebsocket;
}
public class ModConfig extends Packet{
    public String skey;
    @Override
    public Request build() {
        return new Request.Builder()
                .url(Main.getConf().Address.concat("config"))
                .post(RequestBody.create(buildRequestBody(), MediaType.parse("application/json")))
                .build();
    }

    @Override
    public String buildRequestBody() {
        MBean mb=new MBean();
        mb.sessionKey=skey;
        mb.enableWebsocket=true;
        return Main.getGson().toJson(mb);
    }
    @Override
    public String name() {
        return "ModConfig";
    }
    public ModConfig(String skey){
        this.skey=skey;
    }
}
