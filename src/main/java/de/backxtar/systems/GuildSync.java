package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import de.backxtar.gw2.CallGuild;
import de.backxtar.managers.SqlManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GuildSync {

    public static void syncRights(TS3Api api) {
        if (Config.getConfigData().serverGroups == null
                || Config.getConfigData().guildRanks[0].equalsIgnoreCase("0"))
            return;

        List<CallGuild.GWCallGuildMembers> members = CallGuild.getMembers();
        if (members == null) return;

        try {
            ResultSet resultSet = SqlManager.selectAll("API_Keys");

            while (resultSet.next()) {
                String identity = resultSet.getString("clientIdentity");
                String accountName = resultSet.getString("accountName");

                if (members.parallelStream().noneMatch(member -> member.name.equalsIgnoreCase(accountName)) ||
                        !api.isClientOnline(identity)) continue;
                Client client = api.getClientByUId(identity);

                members.parallelStream().forEach(member -> {
                    if (member.name.equalsIgnoreCase(accountName) && !member.rank.equalsIgnoreCase("invited")) {
                        String rank = member.rank;

                        if (Config.getConfigData().serverGroups.containsKey(rank)) {
                            int serverGroupId = Config.getConfigData().serverGroups.get(rank);
                            int[] groups = client.getServerGroups();
                            boolean hasGroup = false;

                            for (int group : groups) {
                                if (group != serverGroupId && !Config.getConfigData().ignoreGroups.contains(group))
                                    api.removeClientFromServerGroup(group, client.getDatabaseId());
                                if (group == serverGroupId) hasGroup = true;
                            }
                            if (!hasGroup) api.addClientToServerGroup(serverGroupId, client.getDatabaseId());
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
