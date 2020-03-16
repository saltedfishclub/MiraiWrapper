package cc.sfclub.polar.mirai.packet;

import cc.sfclub.polar.Core;
import cc.sfclub.polar.mirai.Main;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public abstract class Packet {
    public abstract Request build();
    public abstract String buildRequestBody();
    public abstract String name();
    public String send(){
        try {
            Response response = Main.getHttpClient().newCall(build()).execute();
            if(!response.isSuccessful()){
                Core.getLogger().error("(Server ERROR)Failed to send packet: {} ,Response: {}",name(),response.code());
                response.close();
                return null;
            }
            String ret=response.body().string();
            response.close();
            return ret;
        }catch(IOException e){
            if(Core.getConf().debug){
                e.printStackTrace();
            }
            Core.getLogger().error("(IO ERROR) Failed to send packet: ".concat(name()));
        }
        return null;
    }
}
