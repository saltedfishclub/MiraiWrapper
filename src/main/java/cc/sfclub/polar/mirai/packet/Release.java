package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.mirai.Main;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Release extends Packet{
    private String skey;
    public Release(String skey){
        this.skey=skey;
    }
    @Override
    public Request build() {
        return new Request.Builder()
                .url(Main.getConf().Address.concat("release"))
                .post(RequestBody.create(buildRequestBody(), MediaType.parse("application/json")))
                .build();
    }

    @Override
    public String buildRequestBody() {
        VBean b = new VBean();
        b.qq=Main.getConf().QQ;
        b.sessionKey=skey;
        return Main.getGson().toJson(b);
    }
    @Override
    public String name() {
        return "Release";
    }
}
