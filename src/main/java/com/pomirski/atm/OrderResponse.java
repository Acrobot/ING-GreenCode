package com.pomirski.atm;

import com.dslplatform.json.CompiledJson;

public class OrderResponse {
    public OrderResponse(Order order) {
        this.region = order.region;
        this.atmId = order.atmId;
    }

    @CompiledJson
    public OrderResponse(int region, int atmId) {
        this.region = region;
        this.atmId = atmId;
    }

    public int region;
    public int atmId;
}
