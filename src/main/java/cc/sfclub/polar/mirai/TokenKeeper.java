package cc.sfclub.polar.mirai;

import java.util.TimerTask;

public class TokenKeeper extends TimerTask {
    Main m;

    public TokenKeeper(Main m) {
        this.m = m;
    }

    @Override
    public void run() {
        m.load(Main.getConf().authKey);
    }
}
