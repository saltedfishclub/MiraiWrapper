package cc.sfclub.polar.mirai.commands;

import cc.sfclub.polar.Command;
import cc.sfclub.polar.CommandBase;
import cc.sfclub.polar.events.messages.TextMessage;
import cc.sfclub.polar.mirai.Main;
import cc.sfclub.polar.user.User;

@Command(name="mirai",perm="member.op.wrapper.mirai",description = "Wrapper Mirai.")
public class Mirai extends CommandBase {
    @Override
    public void onCommand(User user, TextMessage textMessage) {
        if(textMessage.getMessage().isEmpty()) {
            textMessage.reply(new String[]{
                    "Mirai Wrapper v1",
                    "Usage:",
                    "mirai reload --reload config"
            });
            return;
        }
        String[] args=textMessage.getMessage().split(" ");
        switch(args[1]){
            case "reload":
                textMessage.reply("Config reloading.");
                Main.loadConfig();
                Main.load(Main.getConf().getAuthKey());
                break;
            default:
                textMessage.reply("Unknown Subcommand.");
                break;
        }
    }
}
