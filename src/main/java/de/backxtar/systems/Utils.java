package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.DerGeraet;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class Utils {
    private static final String lang = Config.getConfigData().lang;
    private static final String setDay = Config.getConfigData().missionDay;
    private static ZoneId zone = ZoneId.systemDefault();

    private static Date dateValue(Instant instant, ZoneId zoneId) {
        return Date.from(instant.atZone(zoneId).toInstant());
    }

    public static LocalDate localDate() {
        return LocalDate.now(zone);
    }

    public static LocalDate getNextDate() {
        DayOfWeek day;

        switch (setDay) {
            case "Mon" : day = DayOfWeek.MONDAY;
                break;
            case "Tue" : day = DayOfWeek.TUESDAY;
                break;
            case "Wed" : day = DayOfWeek.WEDNESDAY;
                break;
            case "Thu" : day = DayOfWeek.THURSDAY;
                break;
            case "Fri" : day = DayOfWeek.FRIDAY;
                break;
            case "Sat" : day = DayOfWeek.SATURDAY;
                break;
            case "Sun" : day = DayOfWeek.SUNDAY;
                break;
            default : day = null;
        }
        return day != null ? localDate().with(TemporalAdjusters.nextOrSame(day)) : null;
    }

    public static String getDate() {
        Instant instant = Instant.now();
        SimpleDateFormat sdfDate = null;
        if (lang.equalsIgnoreCase("de")) sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        if (lang.equalsIgnoreCase("en")) sdfDate = new SimpleDateFormat("MM-dd-yyyy");
        return sdfDate.format(dateValue(instant, zone));
    }

    public static String getDate(String parseFormat) {
        Instant instant = Instant.parse(parseFormat);
        SimpleDateFormat sdfDate = null;
        if (lang.equalsIgnoreCase("de")) sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        if (lang.equalsIgnoreCase("en")) sdfDate = new SimpleDateFormat("MM-dd-yyyy");
        return sdfDate.format(dateValue(instant, zone));
    }

    public static void checkInfo(TS3Api api) {
        String[] infos = api.getChannelInfo(Config.getConfigData().infoChannelID).getName().split(" | ");
        if (infos.length < 2 || !infos[1].equalsIgnoreCase(getDate())) changeInfo();
    }

    public static void changeInfo() {
        TS3Api api = DerGeraet.getInstance().api;
        if (Config.getConfigData().infoChannelID == 0) return;
        int clientSize = 0;

        for (Client client : api.getClients()) {
            if (!client.isServerQueryClient())
                clientSize++;
        }
        String info = "[cspacer0]Clients: " + clientSize + "/" + api.getServerInfo().getMaxClients() + " | " + Utils.getDate();
        if (api.getChannelInfo(Config.getConfigData().infoChannelID).getName().equalsIgnoreCase(info)) return;
        api.editChannel(Config.getConfigData().infoChannelID, ChannelProperty.CHANNEL_NAME, info);
    }
}
