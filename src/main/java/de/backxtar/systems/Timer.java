package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import de.backxtar.managers.SqlManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {

    public static String calcTimestamp(String input) throws NumberFormatException{
        long millisToAdd = 0;

        for (String str : input.split(",")) {
            long l = Long.parseLong(str.substring(0, str.length() - 1));

            switch (str.substring(str.length() - 1).toLowerCase()) {
                case "m" : millisToAdd += l * 1000 * 60;
                    break;
                case "h" : millisToAdd += l * 1000 * 60 * 60;
                    break;
                default :
            }
        }
        return new Timestamp(System.currentTimeMillis() + millisToAdd).toString();
    }

    public static String getTimestamp(String input) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date date = sdf.parse(input);
        return new Timestamp(date.getTime()).toString();
    }

    public static String[] getValues(String input) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH.mm");
        Timestamp timestamp = Timestamp.valueOf(input);
        Date date = new Date(timestamp.getTime());
        String[] values = new String[2];
        values[0] = sdf1.format(date);
        values[1] = sdf2.format(date);
        return values;
    }

    public static void checkTimers(TS3Api api) {
        long curTime = System.currentTimeMillis();

        try {
            ResultSet resultSet = SqlManager.selectAll("Timers");

            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("timeStamp");
                long endTime = timestamp.getTime();

                if (curTime >= endTime) {
                    String UID = resultSet.getString("clientIdentity");
                    String name = resultSet.getString("name");
                    long id = resultSet.getLong("id");
                    String message = "Dein Timer (" + name + ") ist abgelaufen!";

                    if (api.isClientOnline(UID)) api.pokeClient(api.getClientByUId(UID).getId(), message);
                    else api.sendOfflineMessage(UID, "Notification!", message);

                    Object[] valuesDelete = {id, UID, name};
                    SqlManager.delete("Timers", "id = ? AND clientIdentity = ? AND name = ?", valuesDelete);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
