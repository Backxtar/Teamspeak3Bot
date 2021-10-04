package de.backxtar.events;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import de.backxtar.managers.SqlManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OnClientJoin {
    private static final TS3Api api = DerGeraet.getInstance().api;
    private static final Config.Colors colors = Config.getColors();

    public static void sendWelcome(Client client) {
        if (Config.getConfigData().welcomeMessage == 0) return;
        api.sendPrivateMessage(client.getId(),
                "Willkommen auf [b][color=" + colors.mainColor + "]" + api.getServerInfo().getName() + "[color=red][/b], [b]" + client.getNickname() + "[/b]!\n" +
                        "[color=red][b]Note:[/b][/color]  Commands können per [b]privater Nachricht[/b] oder per [b]Channel Nachricht[/b] ausgeführt werden!");
    }

    public static void gw2ApiReminder(Client client) {
        int guest = api.getServerInfo().getDefaultServerGroup();

        for (int serverGroup : client.getServerGroups()) {
            if (serverGroup == guest) {
                api.sendPrivateMessage(client.getId(),
                        "\nBist du ein Freund der Gilde? Dann warte in einem [color=" + colors.mainColor + "][b]Support-Channel[/b][/color] auf Hilfe!\n" +
                                "Bitte beachte, dass Du nach [color=" + colors.mainColor + "][b]5 Minuten[/b][/color] vom Server geworfen wirst, " +
                                "wenn Du [color=" + colors.mainColor + "][b]keine Server-Gruppe[/b][/color] hast und Dich " +
                                "[color=" + colors.mainColor + "][b]in der Lobby[/b][/color] befindest!\n" +
                                "\nBist du Mitglied der Gilde? Gebe [color=" + colors.mainColor + "][b]!key youAPIKey[/b][/color] ein!" +
                                "\nDu kannst hier einen [b][color=red]Gw2-Key[/color][b] erstellen: https://account.arena.net/applications");
                return;
            }
        }

        try {
            String[] fieldsSelect = {"GW2_Key"};
            Object[] valuesSelect = {client.getUniqueIdentifier()};
            ResultSet resultSet = SqlManager.select(fieldsSelect, "API_Keys", "clientIdentity = ?", valuesSelect);
            if (resultSet.next()) return;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        api.pokeClient(client.getId(), "[color=red]✘[/color] Du hast noch keinen [b][color=red]Gw2-Key[/color][b] hinterlegt!");
        api.sendPrivateMessage(client.getId(), "[color=red]✘[/color] Du hast noch keinen [b][color=red]Gw2-Key[/color][b] hinterlegt\n" +
                "Bitte denke daran einen [b][color=red]Gw2-Key[/color][b] zu hinterlegen. Du kannst hier einen [b][color=red]Gw2-Key[/color][b] erstellen:\n" +
                "https://account.arena.net/applications");
    }
}
