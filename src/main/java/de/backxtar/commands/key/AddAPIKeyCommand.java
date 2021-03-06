package de.backxtar.commands.key;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.managers.CommandInterface;
import de.backxtar.gw2.CallToken;

public class AddAPIKeyCommand implements CommandInterface {
    private final Config.Colors colors = Config.getColors();

    @Override
    public void run(String[] cmdValues, TS3Api api, TextMessageEvent event, Client client) {
        if (cmdValues.length > 1) {
            CallToken.checkToken(client, cmdValues[1]);
            return;
        }
        String[] gw2Values = CallToken.isValid(client);
        if (gw2Values == null) return;

        CallToken.GWCallToken token = CallToken.getGWCallToken(gw2Values[0]);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < token.permissions.length; i++) {
            builder.append(token.permissions[i]);
            if (i < (token.permissions.length - 1)) builder.append(", ");
        }
        api.sendPrivateMessage(client.getId(),
                "\n[color=" + colors.mainColor + "][b]Gw2-Account:[/b][/color] " + gw2Values[1] +
                        "\n[color=" + colors.mainColor + "][b]Token:[/b][/color] " + token.name +
                        "\n[color=" + colors.mainColor + "][b]Permissions:[/b][/color] " + builder);
    }
}
