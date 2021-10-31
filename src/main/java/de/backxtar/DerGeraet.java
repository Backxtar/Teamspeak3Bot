package de.backxtar;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3Exception;
import de.backxtar.gw2.CallToken;
import de.backxtar.managers.CommandManager;
import de.backxtar.managers.EventManager;
import de.backxtar.managers.SqlManager;
import de.backxtar.systems.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
* Author: Josh Quick
* A Guild Wars 2 Teamspeak3 Bot
*/

public class DerGeraet {
    private final ScheduledExecutorService scheduler;
    private static final Logger logger = LoggerFactory.getLogger(DerGeraet.class);
    private static DerGeraet ts3Bot;
    private final CommandManager commandManager;
    //private final NewConfig config;
    private ConfigJSON.Config cfg;
    public TS3Query query;
    public TS3Api api;

    public DerGeraet() throws IOException, TS3Exception, SQLException, ClassNotFoundException {
        ts3Bot = this;
        this.scheduler = Executors.newScheduledThreadPool(5);
        //this.config = new NewConfig();
        this.cfg = ConfigJSON.loadFromJSON();
        final TS3Config config = new TS3Config();
        Config.loadConfig();
        logger.info(Config.getFile().getName() + " loaded.");

        config.setHost(Config.getConfigData().ts3Host);
        config.setEnableCommunicationsLogging(true);
        config.setFloodRate(TS3Query.FloodRate.UNLIMITED);

        query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        api.login(Config.getConfigData().ts3Username, Config.getConfigData().ts3Password);
        api.selectVirtualServerById(1);
        api.setNickname(Config.getConfigData().ts3Nickname);
        logger.info("Configuration successful. Query connected.");

        SqlManager.connect();
        EventManager.loadEvents();
        this.commandManager = new CommandManager();
        scheduleTasks();

        logger.info(Config.getConfigData().ts3Nickname + " online - connected to " + DerGeraet.ts3Bot.api.getServerInfo().getName() + ".");
        logger.info("UID: " + api.whoAmI().getUniqueIdentifier() + " | ID: " + api.whoAmI().getId());
        initShutdown();
    }

    public static void main(String[] args) {
        try {
            new DerGeraet();
        } catch (IOException | TS3Exception | SQLException | ClassNotFoundException e) {
            logger.info("Configuration failed!");
            logger.info("Please check config.cfg");
            e.printStackTrace();
        }
    }

    private void initShutdown() {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("exit")) {
                    if (DerGeraet.ts3Bot.query.isConnected() && !DerGeraet.ts3Bot.scheduler.isShutdown()) {
                        DerGeraet.ts3Bot.scheduler.shutdownNow();
                        DerGeraet.ts3Bot.query.exit();
                        SqlManager.disconnect();
                        logger.info("Bot offline.");
                        System.exit(0);
                    } else logger.info("Can not shutdown! Please terminate the screen.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scheduleTasks() {
        scheduler.scheduleAtFixedRate(() -> AfkMover.checkAfk(api), 1, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {
            ClientDescCheck.descChange(api);
            GuildSync.syncRights(api);
        },2, 60, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {
            UnwantedGuest.checkGuests(api);
            Utils.checkInfo(api);
            ClientHelpReminder.unlockChannel(api);
            AfkMover.checkOnline(api);
            Timer.checkTimers(api);
        }, 3, 60, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {
            ExchangeCheck.checkExchange(api);
            ArcDpsCheck.checkArcDpsVersion(api);
            DailyCheck.checkDailies(api);
            GuildInfo.loadGuildInfo(api);
        }, 4, 300, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {
            api.getClients().forEach(CallToken::checkToken);
            ChangeServerMessage.changeMessage(api);
        }, 5, 600, TimeUnit.SECONDS);
    }

    public static DerGeraet getInstance() {
        return ts3Bot;
    }

    public CommandManager getCmdManager() {
        return commandManager;
    }

    public ConfigJSON.Config getCfg() {
        return cfg;
    }

    /*public NewConfig getConfig() {
        return config;
    }*/
}
