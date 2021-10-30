package de.backxtar.managers;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.commands.info.BossesCommand;
import de.backxtar.commands.info.MeCommand;
import de.backxtar.commands.info.WalletCommand;
import de.backxtar.commands.key.AddAPIKeyCommand;
import de.backxtar.commands.key.RemoveAPIKeyCommand;
import de.backxtar.commands.manage.HelpCommand;
import de.backxtar.commands.manage.UIDCommand;
import de.backxtar.commands.timer.CancelTimerCommand;
import de.backxtar.commands.timer.ShowTimersCommand;
import de.backxtar.commands.timer.StartTimerCommand;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    private final ConcurrentHashMap<String, CommandInterface> commands;

    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();

        // Command-Register
        this.commands.put("help", new HelpCommand());
        this.commands.put("key", new AddAPIKeyCommand());
        this.commands.put("keyremove", new RemoveAPIKeyCommand());
        this.commands.put("uid", new UIDCommand());
        this.commands.put("me", new MeCommand());
        this.commands.put("wallet", new WalletCommand());
        this.commands.put("bosses", new BossesCommand());
        this.commands.put("timer", new StartTimerCommand());
        this.commands.put("timers", new ShowTimersCommand());
        this.commands.put("cancel", new CancelTimerCommand());
        //this.commands.put("ufe", new UfeCommand());
    }

    public boolean runCmd(String[] command, TS3Api api, TextMessageEvent event, Client client) {
        CommandInterface cmdInterface;

        if ((cmdInterface = this.commands.get(command[0].toLowerCase())) != null) {
            cmdInterface.run(command, api, event, client);
            return true;
        }
        return false;
    }

    public static String[] splitArgs(String message, int values) {
        return message.split(" ", values);
    }
}
