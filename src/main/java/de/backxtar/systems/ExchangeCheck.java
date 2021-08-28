package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import de.backxtar.gw2.CallExchange;
import de.backxtar.gw2.Gw2Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExchangeCheck {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static TS3Api api = DerGeraet.getInstance().api;

    public static void checkExchange() {
        String urlTP = "https://wiki.guildwars2.com/images/thumb/d/df/Black-Lion-Logo.png/300px-Black-Lion-Logo.png";
        String urlGem = "http://i.epvpimg.com/BLondab.png";
        String urlGold = "http://i.epvpimg.com/JEyBcab.png";
        String urlSilver = "http://i.epvpimg.com/ieqSaab.png";
        String urlCopper = "http://i.epvpimg.com/I8jDgab.png";

        Future<CallExchange.GWCallExchange> coinsAsync = executor.submit(() -> CallExchange.getCoins(true));
        Future<CallExchange.GWCallExchange> gemsAsync = executor.submit(() -> CallExchange.getCoins(false));
        CallExchange.GWCallExchange coins;
        CallExchange.GWCallExchange gems;

        try {
            coins = coinsAsync.get();
            gems = gemsAsync.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
        long[] result = Gw2Utils.getCoins(gems.quantity);
        String resultExchange = (result[2] > 0 ? "[b]" + result[2] + "[/b][img]" + urlGold + "[/img]" : "") +
                (result[1] > 0 ? "[b]" + result[1] + "[/b][img]" + urlSilver + "[/img]" : "") +
                (result[0] > 0 ? "[b]" + result[0] + "[/b][img]" + urlCopper + "[/img]" : "");

        String desc = "[center][size=11][URL=client://259/serveradmin]Message me![/URL][/size]" +
                "\n[img]" + urlTP + "[/img]\n" +
                "[size=11][b]100[/b][img]" + urlGem + "[/img] entsprechen: " + resultExchange + "\n" +
                "[b]100[/b][img]" + urlGold + "[/img] entsprechen: [b]" + coins.quantity + "[/b][img]" + urlGem + "[/img]";
        if (api.getChannelInfo(Config.getConfigData().infoChannelID).getDescription().equalsIgnoreCase(desc)) return;
        api.editChannel(Config.getConfigData().infoChannelID, ChannelProperty.CHANNEL_DESCRIPTION, desc);
    }
}
