package de.backxtar.commands.manage;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.managers.CommandInterface;

public class HelpCommand implements CommandInterface {
    private final Config.Colors colors = Config.getColors();
    private final String lang = Config.getConfigData().lang;

    @Override
    public void run(String[] cmdValues, TS3Api api, TextMessageEvent event, Client client) {
        String mess = "";

        if (lang.equalsIgnoreCase("de")) mess = "\n" +
                "Hier ist Deine angeforderte Hilfe, [b]" + client.getNickname() + "[/b]:\n" +
                "╰ [color=" + colors.mainColor + "][b]!key <optional=yourAPIKey>[/b][/color] ⎯  Zeigt, speichert oder aktualisiert Deinen [b]Gw2-Key[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!keyremove[/b][/color] ⎯  Löscht Deinen [b]Gw2-Key[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!uid[/b][/color] ⎯  Zeigt Dir Deine [b]UID[/b] an\n" +
                "╰ [color=" + colors.mainColor + "][b]!timer 1h,2m name | 11.11.2011-13:30 name[/b][/color] ⎯  Startet einen [b]Timer[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!timers[/b][/color] ⎯  Zeigt Dir Deine [b]Timer[/b] an\n" +
                "╰ [color=" + colors.mainColor + "][b]!cancel timer[/b][/color] ⎯  Bricht Deinen [b]Timer[/b] ab\n" +
                "╰ [color=" + colors.mainColor + "][b]!me[/b][/color] ⎯  Gibt Dir Deine [b]Gw2-Accountinformationen[/b] aus\n" +
                "╰ [color=" + colors.mainColor + "][b]!wallet[/b][/color] ⎯  Zeigt Dir Deine [b]Gw2-Geldbörse[/b] an\n" +
                "╰ [color=" + colors.mainColor + "][b]!bosses[/b][/color] ⎯  Zeigt Dir Deine wöchentliche [b]Gw2-Raid-Überischt[/b] an\n" +
                "";
        if (lang.equalsIgnoreCase("en")) mess = "\n" +
                "Here is your requested help, [b]" + client.getNickname() + "[/b]:\n" +
                "╰ [color=" + colors.mainColor + "][b]!key <optional=yourAPIKey>[/b][/color] ⎯  Show, store or update your [b]Gw2-Key[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!keyremove[/b][/color] ⎯  Remove your [b]Gw2-Key[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!uid[/b][/color] ⎯  Show your [b]UID[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!timer 1h,2m name | 11.11.2011-13:30 name[/b][/color] ⎯  Start a [b]timer[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!timers[/b][/color] ⎯  Show your [b]timers[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!cancel timer[/b][/color] ⎯  Cancel your [b]timer[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!me[/b][/color] ⎯  Show your [b]Gw2-Accountinformation[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!wallet[/b][/color] ⎯  Show your [b]Gw2-Wallet[/b]\n" +
                "╰ [color=" + colors.mainColor + "][b]!bosses[/b][/color] ⎯  Show your weekly [b]Gw2-Raid-Clears[/b]\n" +
                "";
        api.sendPrivateMessage(client.getId(), mess);
    }
}
