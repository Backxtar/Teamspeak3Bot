package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import de.backxtar.Config;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ChangeServerMessage {

    public static void changeMessage(TS3Api api) {
        String curMes = api.getServerInfo().getWelcomeMessage(), mes;
        SimpleDateFormat sdf = null;

        LocalDate now = Utils.localDate();
        LocalDate nextEvent = Utils.getNextDate();
        long value = Duration.between(now.atStartOfDay(), nextEvent.atStartOfDay()).toDays();

        //Events
        LocalDate halloween = LocalDate.of(now.getYear(), 10, 31);
        LocalDate christmas = LocalDate.of(now.getYear(), 12, 24);
        LocalDate newYear   = LocalDate.of(now.getYear(), 1, 1);

        if (Config.getConfigData().lang.equalsIgnoreCase("de"))
            sdf = new SimpleDateFormat("dd.MM.yyyy");
        if (Config.getConfigData().lang.equalsIgnoreCase("en"))
            sdf = new SimpleDateFormat("MM-dd-yyyy");

        if (sdf == null) return;
        mes = Config.getConfigData().missionMessage.replaceAll("{date}", sdf.format(Utils.getNextDate()));
        if (curMes.equalsIgnoreCase(mes)) return;

        final Map<VirtualServerProperty, String> properties = new HashMap<>();
        properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, mes);
        if (value < 3) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTBANNER_MODE, "MODAL");
        else properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTBANNER_MODE, "NONE");
        api.editServer(properties);
    }
}
