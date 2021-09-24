package de.backxtar.gw2;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.gson.Gson;
import de.backxtar.managers.SqlManager;
import de.backxtar.DerGeraet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CallToken {
    private static TS3Api api = DerGeraet.getInstance().api;

    public static class GWCallToken {
        public String id;
        public String name;
        public String[] permissions;
    }

    public static GWCallToken getGWCallToken(String token) {
        String json = "";
        int fails = 0, maxFails = 3;

        while (fails != maxFails) {
            try {
                json = Gw2Utils.getJson("https://api.guildwars2.com/v2/tokeninfo?access_token=" + token);
                fails = 3;
            } catch (IOException ignored) {
                ++fails;
            }
        }
        Gson gson = new Gson();

        try {
            return gson.fromJson(json, GWCallToken.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static void checkToken(Client client) {
        GWCallToken token;

        try {
            String[] fieldsSelect = {"GW2_Key"};
            Object[] valuesSelect = {client.getUniqueIdentifier()};
            ResultSet resultSet = SqlManager.select(fieldsSelect, "API_Keys", "clientIdentity = ?", valuesSelect);

            if (resultSet.next()) {
                token = getGWCallToken(resultSet.getString("GW2_Key"));

                if (token == null || token.permissions.length < 10) {
                    if (api.isClientOnline(client.getUniqueIdentifier())) {
                        api.sendPrivateMessage(client.getId(),
                                "\n" +
                                        "[color=red]✘[/color] Dein [b][color=red]Gw2-Key[/color][/b] ist nicht mehr gültig oder " +
                                        "hat nicht alle Berechtigungen.\n" +
                                        "Du kannst hier einen neuen Gw2-Key erstellen:\n" +
                                        "https://account.arena.net/applications");
                        return;
                    }
                    api.sendOfflineMessage(client.getUniqueIdentifier(),
                            "\n" +
                                    "Gw2_Key ungültig!",
                            "[color=red]✘[/color] Dein [b][color=red]Gw2-Key[/color][/b] ist nicht mehr gültig oder " +
                                    "hat nicht alle Berechtigungen.\n" +
                                    "Du kannst hier einen neuen Gw2-Key erstellen:\n" +
                                    "https://account.arena.net/applications");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkToken(Client client, String apiKey) {
        GWCallToken token;
        CallAccount.GWCallAccount account;

        try {
            String[] fieldsSelect = {"GW2_Key"};
            Object[] valuesSelect = {client.getUniqueIdentifier()};
            ResultSet resultSet = SqlManager.select(fieldsSelect, "API_Keys", "clientIdentity = ?", valuesSelect);

            if (resultSet.next()) {
                if (resultSet.getString("GW2_Key").equalsIgnoreCase(apiKey)) {
                    api.sendPrivateMessage(client.getId(),
                            "\n" +
                                    "[color=red]✘[/color] Dieser [b][color=red]Gw2-Key[/color][/b] ist bereits hinterlegt.");
                    return;
                }
                token = getGWCallToken(apiKey);

                if (token != null && token.permissions.length == 10) {
                    account = CallAccount.getAccount(apiKey);

                    String[] fieldsUpdate = {"GW2_Key", "accountName"};
                    Object[] valuesUpdate = {apiKey, account.name, client.getUniqueIdentifier()};
                    SqlManager.update(fieldsUpdate, "API_Keys", "clientIdentity = ?", valuesUpdate);

                    api.sendPrivateMessage(client.getId(),
                            "\n" +
                                    "[color=green]✔[/color] Dein [b][color=green]Gw2-Key[/color][/b] wurde aktualisiert.\n" +
                                    "Gw2-Account: " + account.name);
                    return;
                }
            } else {
                token = getGWCallToken(apiKey);

                if (token != null && token.permissions.length == 10) {
                    account = CallAccount.getAccount(apiKey);

                    String[] fieldsInsert = {"clientIdentity", "GW2_Key", "accountName"};
                    Object[] valuesInsert = {client.getUniqueIdentifier(), apiKey, account.name};
                    SqlManager.insert("API_Keys", fieldsInsert, valuesInsert);

                    api.sendPrivateMessage(client.getId(),
                            "\n" +
                                    "[color=green]✔[/color] Dein [b][color=green]Gw2-Key[/color][/b] wurde hinterlegt.\n" +
                                    "Gw2-Account: " + account.name);
                    return;
                }
            }
            api.sendPrivateMessage(client.getId(),
                    "\n" +
                            "[color=red]✘[/color] Dein [b][color=red]Gw2-Key[/color][/b] ist nicht gültig oder " +
                            "hat nicht alle Berechtigungen.\n" +
                            "Du kannst hier einen neuen Gw2-Key erstellen:\n" +
                            "https://account.arena.net/applications");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String[] isValid(Client client) {
        String[] gw2Values = new String[2];
        // Slot 1: Gw2-Key
        // Slot 2: accountName

        try {
            String[] fieldsSelect = {"GW2_Key", "accountName"};
            Object[] valuesSelect = {client.getUniqueIdentifier()};
            ResultSet resultSet = SqlManager.select(fieldsSelect, "API_Keys", "clientIdentity = ?", valuesSelect);

            if (resultSet.next()) {
                gw2Values[0] = resultSet.getString("GW2_Key");
                gw2Values[1] = resultSet.getString("accountName");
                GWCallToken token = getGWCallToken(gw2Values[0]);

                if (token != null && token.permissions.length == 10) return gw2Values;
                else {
                    api.sendPrivateMessage(client.getId(),
                            "\n" +
                                    "[color=red]✘[/color] Dein [b][color=red]Gw2-Key[/color][/b] ist ungültig oder hat nicht alle Berechtigungen.");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        api.sendPrivateMessage(client.getId(),
                "\n" +
                "[color=red]✘[/color] Du hast noch keinen [b][color=red]Gw2-Key[/color][/b] hinterlegt.");
        return null;
    }
}
