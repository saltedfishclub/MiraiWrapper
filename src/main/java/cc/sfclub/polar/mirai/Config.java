package cc.sfclub.polar.mirai;

import lombok.Getter;
import org.mve.plugin.java.JavaPlugin;
import org.mve.plugin.java.PluginConfig;

public class Config extends PluginConfig {
    @Getter
    public String authKey = "awa";
    @Getter
    public String Address = "http://localhost:8080/";
    @Getter
    public long QQ;
    @Getter
    public boolean displayMessage = true;

    public Config(JavaPlugin p) {
        super(p);
    }
}
