package com.pomirski.onlinegame;

import com.dslplatform.json.CompiledJson;

@CompiledJson
public class Clan implements Comparable<Clan> {
    public short numberOfPlayers;
    public short points;

    @Override
    public int compareTo(Clan o) {
        int regionComparison = -Integer.compare(this.points, o.points);
        if (regionComparison != 0) {
            return regionComparison;
        }

        return Integer.compare(this.numberOfPlayers, o.numberOfPlayers);
    }
}
