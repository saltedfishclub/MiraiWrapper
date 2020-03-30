package cc.sfclub.polar.mirai;

import java.util.TimerTask;

public class TokenKeeper extends TimerTask {
    @Override
    public void run() {
        Main.load(Main.getConf().authKey);
    }
}
