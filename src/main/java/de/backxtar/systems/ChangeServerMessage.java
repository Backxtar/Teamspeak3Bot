package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import de.backxtar.Config;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ChangeServerMessage {

    public static void changeMessage(TS3Api api) {
        try {
            if (Config.getConfigData().missionDay == null) return;
            String curMes = api.getServerInfo().getWelcomeMessage(), mes, date = "";

            LocalDate now = Utils.localDate();
            LocalDate nextEvent = Utils.getNextDate();
            long value = Duration.between(now.atStartOfDay(), nextEvent.atStartOfDay()).toDays();

            //Events
            LocalDate halloween = LocalDate.of(now.getYear(), 10, 31);
            LocalDate christmas = LocalDate.of(now.getYear(), 12, 24);
            LocalDate newYear = LocalDate.of(now.getYear(), 1, 1);

            if (Config.getConfigData().lang.equalsIgnoreCase("de"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (Config.getConfigData().lang.equalsIgnoreCase("en"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

            if (date.isEmpty()) return;
            mes = Config.getConfigData().missionMessage.replaceAll("%date", date);
            if (curMes.equalsIgnoreCase(mes)) return;

            final Map<VirtualServerProperty, String> properties = new HashMap<>();
            properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, mes);
            if (value < 3) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "2");
            else properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "0");

            api.editServer(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
