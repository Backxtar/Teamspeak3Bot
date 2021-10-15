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
    private static final ZoneId zone = ZoneId.systemDefault();

    private static Date dateValue(Instant instant) {
        return Date.from(instant.atZone(zone).toInstant());
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

    public static LocalDate getEasterSunday() {
        final int y = Year.now().getValue();
        final int a = y % 19;
        final int b = y / 100;
        final int c = y % 100;
        final int d = b / 4;
        final int e = b % 4;
        final int f = (b + 8) / 25;
        final int g = (b - f + 1) / 3;
        final int h = (19 * a + b - d - g + 15) % 30;
        final int i = c / 4;
        final int k = c % 4;
        final int m = (32 + 2 * e + 2 * i - h - k) % 7;
        final int n = (a + 11 * h + 22 * m) / 451;
        final int month = (h + m - 7 * n + 114) / 31;
        final int day = (((h + m - (7 * n) + 114) % 31) + 1);
        return LocalDate.of(y, month, day);
    }

    public static String getDate() {
        Instant instant = Instant.now();
        SimpleDateFormat sdfDate = null;
        if (lang.equalsIgnoreCase("de")) sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        if (lang.equalsIgnoreCase("en")) sdfDate = new SimpleDateFormat("MM-dd-yyyy");
        return sdfDate.format(dateValue(instant));
    }

    public static String getDate(String parseFormat) {
        Instant instant = Instant.parse(parseFormat);
        SimpleDateFormat sdfDate = null;
        if (lang.equalsIgnoreCase("de")) sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        if (lang.equalsIgnoreCase("en")) sdfDate = new SimpleDateFormat("MM-dd-yyyy");
        return sdfDate.format(dateValue(instant));
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
