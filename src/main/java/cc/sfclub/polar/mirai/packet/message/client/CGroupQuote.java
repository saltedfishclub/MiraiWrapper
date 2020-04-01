package cc.sfclub.polar.mirai.packet.message.client;

import lombok.Getter;
import lombok.Setter;

public class CGroupQuote extends CGroupPlain {
    @Getter
    @Setter
    public long quote;
}
