package cc.sfclub.polar.mirai.packet;

public enum Type {
    NORMAL(0), WRONG_KEY(1), BOT_NOT_EXIST(2), SESSION_INVAILD(3), SESSION_NOT_AUTHORIZED(4), RECEIVER_NOT_EXIST(5), PERMISSION_DENIED(10), INVAILD(40), SERVER_ERROR(-1);

    Type(int i) {
    }
}
