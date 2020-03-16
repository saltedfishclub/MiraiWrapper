package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.mirai.Main;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

class Bean{
    String authKey;
}
class MResponse{
    int code;
    String session;
}
public class Authorize extends Packet{
    private String AuthKey;
    @Override
    public Request build() {
        return new Request.Builder()
                .url(Main.getConf().Address.concat("auth"))
                .post(RequestBody.create(buildRequestBody(),MediaType.parse("application/json")))
                .build();
    }
    @Override
    public String buildRequestBody() {
        Bean b = new Bean();
        b.authKey = AuthKey;
        return Main.getGson().toJson(b);
    }
    @Override
    public String send(){
        String raw=super.send();
        if(raw==null){
            return null;
        }
        MResponse r=Main.getGson().fromJson(raw, MResponse.class);
        if(r.code==0){
            return r.session;
        }
        return null;
    }
    @Override
    public String name() {
        return "Authorize";
    }

    public Authorize(String key){
        AuthKey=key;
    }

}
