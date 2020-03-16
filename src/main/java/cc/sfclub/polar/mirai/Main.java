package cc.sfclub.polar.mirai;

import cc.sfclub.polar.Core;
import cc.sfclub.polar.mirai.commands.Mirai;
import cc.sfclub.polar.mirai.packet.*;
import com.google.gson.Gson;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.mve.plugin.java.JavaPlugin;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin{
    Bot bot=new Bot();
    @Getter
    private static OkHttpClient httpClient=new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build();
    @Getter
    private static Gson Gson = new Gson();
    private static File config;
    @Getter
    private static Config conf;
    @Getter
    private static String Session;
    private static WebSocket ws;
    @Override
    public void onEnable() {
        Core.getInstance().addBot(bot);
        config = new File(this.getDataFolder().getAbsolutePath() + "/mirai.json");
        if(!config.exists()){
            Config a=new Config();
            try {
                byte[] bWrite = Gson.toJson(a).getBytes();
                OutputStream os = new FileOutputStream(config);
                for (byte b : bWrite) {
                    os.write(b);
                }
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            conf=a;
            Core.getLogger().warn("Please Set the AuthKEY");
            return;
        }
        if(!loadConfig()){
            return;
        }
        if(load(conf.authKey)){
            Core.getInstance().getCommandManager().register(new Mirai());
        }
    }
    @Override
    public void onDisable(){
        if (Session != null) {
            new Release(getSession()).send();
        }
        if (ws != null) {
            ws.close(1000, "onDisable");
        }
    }
    public static boolean load(String authkey){
        if(Session!=null){
            new Release(getSession()).send();
        }
        String session = new Authorize(authkey).send();
        if(session==null){
            Core.getLogger().error("Auth Failed.(Null Response OR Wrong AuthKey)");
            return false;
        }
        Type t=new Verify().send(session);
        if(t != Type.NORMAL){
            Core.getLogger().error(t.name());
            return false;
        }
        new ModConfig(session).send(); //Modify the server config for websocket support
        Session=session;
        if(ws!=null){
            ws.close(1000,"onDisable");
        }
        connect();
        return true;
    }
    private static void connect() {

        Request request = new Request.Builder()
                .url(conf.getAddress().replaceAll("http","ws").concat("message?sessionKey=").concat(getSession()))
                .addHeader("Sec-Websocket-Key", UUID.randomUUID().toString())
                .build();
        Core.getLogger().info("[QQ] Connecting to {}",conf.getAddress().replaceAll("http","ws"));
        ws=httpClient.newWebSocket(request, new WSSListener());

    }
    public static boolean loadConfig(){
        try {
            InputStream f = new FileInputStream(config);
            int size = f.available();
            StringBuilder confText = new StringBuilder();
            for (int i = 0; i < size; i++) {
                confText.append((char) f.read());
            }
            conf = Gson.fromJson(confText.toString(), Config.class);
            if(conf.authKey.isEmpty()){
                Core.getLogger().error("Please type the AUTHKEY");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
