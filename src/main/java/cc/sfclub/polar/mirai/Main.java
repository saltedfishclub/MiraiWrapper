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

import java.io.File;
import java.util.Timer;
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
    @Getter
    private static Config conf;
    @Getter
    private static String Session;
    private WebSocket ws;
    private Timer tokenKeeper;

    public boolean load(String authkey) {
        if (Session != null) {
            new Release(getSession()).send();
        }
        String session = new Authorize(authkey).send();
        if (session == null) {
            Core.getLogger().error("Auth Failed.(Null Response OR Wrong AuthKey)");
            return false;
        }
        Type t = new Verify().send(session);
        if (t != Type.NORMAL) {
            Core.getLogger().error(t.name());
            return false;
        }
        new ModConfig(session).send(); //Modify the server config for websocket support
        Session = session;
        if (ws != null) {
            ws.close(1000, "onDisable");
        }
        connect();
        return true;
    }

    @Override
    public void onEnable() {
        Core.getInstance().addBot(bot);
        File config = new File(this.getDataFolder().getAbsolutePath() + "/config.json");
        if (!config.exists()) {
            Config a = new Config(this);
            a.saveConfig();
            Core.getLogger().warn("Please Set the AuthKEY");
            return;
        }
        conf = (Config) new Config(this).reloadConfig();
        if (conf.getAuthKey().isEmpty()) {
            Core.getLogger().warn("Set the authkey please.");
            return;
        }
        if (tokenKeeper != null) {
            tokenKeeper.cancel();
        }
        tokenKeeper = new Timer();
        tokenKeeper.schedule(new TokenKeeper(this), 0, 900 * 1000);
        Core.getInstance().getCommandManager().register(new Mirai());
    }

    @Override
    public void onDisable() {
        tokenKeeper.cancel();
        if (Session != null) {
            new Release(getSession()).send();
        }
        if (ws != null) {
            ws.close(1000, "onDisable");
        }
    }

    private void connect() {
        Request request = new Request.Builder()
                .url(conf.getAddress().replaceAll("http", "ws").concat("message?sessionKey=").concat(getSession()))
                .addHeader("Sec-Websocket-Key", UUID.randomUUID().toString())
                .build();
        Core.getLogger().info("[QQ] Connecting to {}", conf.getAddress().replaceAll("http", "ws"));
        ws = httpClient.newWebSocket(request, new WSSListener());
    }
}
