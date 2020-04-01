package cc.sfclub.polar.mirai.commands;

import cc.sfclub.polar.Command;
import cc.sfclub.polar.CommandBase;
import cc.sfclub.polar.events.messages.TextMessage;
import cc.sfclub.polar.user.User;

@Command(name="mirai",perm="member.op.wrapper.mirai",description = "Wrapper Mirai.")
public class Mirai extends CommandBase {
    @Override
    public void onCommand(User user, TextMessage textMessage) {
            textMessage.reply("Mirai Wrapper Working!");
    }
}
