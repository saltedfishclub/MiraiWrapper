package cc.sfclub.polar.mirai.packet.message;

import lombok.Data;

@Data
public class Sender {
    public long id;
    public String memberName;
    public String permission;
    public Group group;
}
