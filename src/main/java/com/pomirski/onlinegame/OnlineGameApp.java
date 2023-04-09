package com.pomirski.onlinegame;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.runtime.Settings;
import com.pomirski.atm.Order;
import com.pomirski.atm.OrderResponse;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqLengthAware;
import org.takes.rs.RsText;
import org.takes.rs.RsWithType;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class OnlineGameApp implements Take {
    private static final int BUFFER_SIZE = 1024;
    private final DslJson<Object> dslJson;

    public OnlineGameApp() {
        this.dslJson = new DslJson<>(Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());
    }

    @Override
    public Response act(Request request) throws Exception {
        Request lengthAware = new RqLengthAware(request);

        CalculateRequest calculateRequest = dslJson.deserialize(CalculateRequest.class, lengthAware.body());

        List<List<Clan>> groups = new LinkedList<>();

        if (calculateRequest != null) {
            Collections.sort(calculateRequest.clans);

            while (!calculateRequest.clans.isEmpty()) {
                List<Clan> currentGroup = new LinkedList<>();
                int currentPlayerCount = 0;

                ListIterator<Clan> clanIterator = calculateRequest.clans.listIterator();

                while (clanIterator.hasNext()) {
                    Clan clan = clanIterator.next();

                    if (currentPlayerCount + clan.numberOfPlayers > calculateRequest.groupCount) {
                        continue;
                    }

                    currentGroup.add(clan);
                    clanIterator.remove();

                    currentPlayerCount += clan.numberOfPlayers;

                    if (currentPlayerCount == calculateRequest.groupCount) {
                        break;
                    }
                }

                groups.add(currentGroup);
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
        dslJson.serialize(groups, os);

        return new RsWithType(new RsText(os.toByteArray()), "application/json");
    }
}
