package com.pomirski.onlinegame;

import com.dslplatform.json.CompiledJson;

import java.util.LinkedList;

@CompiledJson
public class CalculateRequest {
    public int groupCount;
    public LinkedList<Clan> clans;
}
