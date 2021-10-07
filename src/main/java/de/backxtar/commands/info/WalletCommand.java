package de.backxtar.commands.info;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.managers.CommandInterface;
import de.backxtar.gw2.CallToken;
import de.backxtar.gw2.CallWallet;
import de.backxtar.gw2.Gw2Utils;

import java.util.List;

public class
WalletCommand implements CommandInterface {

    @Override
    public void run(String[] cmdValues, TS3Api api, TextMessageEvent event, Client client) {
        String[] gw2Values = CallToken.isValid(client);
        if (gw2Values == null) return;

        List<CallWallet.GWCallWallet> wallet = CallWallet.getWallet(gw2Values[0], client);

        if (wallet == null) return;
        StringBuilder currencies = new StringBuilder();

        for (int i = 0; i < wallet.size(); i++) {
            currencies.append(Gw2Utils.currency(wallet.get(i).id, wallet.get(i).value));
            if (i < (wallet.size() -1)) currencies.append("\n");
        }

        String toSend = "";
        if (Config.getConfigData().lang.equalsIgnoreCase("de")) {
            toSend = "\nHier ist deine Geldbörse, [b]" + client.getNickname() + "[/b]\n\n" +
                    "[color=" + Config.getColors().mainColor + "][b]Gw2-Account:[/b][/color] " + gw2Values[1] + "\n" + currencies;
        }
        if (Config.getConfigData().lang.equalsIgnoreCase("en")) {
            toSend = "\nHere is your wallet, [b]" + client.getNickname() + "[/b]\n\n" +
                    "[color=" + Config.getColors().mainColor + "][b]Gw2-Account:[/b][/color] " + gw2Values[1] + "\n" + currencies;
        }
        api.sendPrivateMessage(client.getId(), toSend);
    }
}
