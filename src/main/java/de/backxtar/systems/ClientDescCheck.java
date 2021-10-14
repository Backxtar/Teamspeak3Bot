package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.managers.SqlManager;
import de.backxtar.DerGeraet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDescCheck {

    public static void descChange(TS3Api api) {

        try {
            ResultSet resultSet = SqlManager.selectAll("API_Keys");

            while (resultSet.next()) {
                String clientIdentity = resultSet.getString("clientIdentity");
                String accountName = resultSet.getString("accountName");

                if (!api.isClientOnline(clientIdentity)) continue;
                Client client = api.getClientByUId(clientIdentity);

                if (api.getClientInfo(client.getId()).getDescription().equalsIgnoreCase(accountName)) continue;
                api.editClient(client.getId(), ClientProperty.CLIENT_DESCRIPTION, accountName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
