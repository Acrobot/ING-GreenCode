package com.pomirski.atm;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.rq.RqLengthAware;
import org.takes.rs.*;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class AtmApp implements Take {
    private static final int BUFFER_SIZE = 1024;
    private final DslJson<Object> dslJson;

    public AtmApp() {
        this.dslJson = new DslJson<>(Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());
    }

    @Override
    public Response act(Request request) throws Exception {
        Request lengthAware = new RqLengthAware(request);
        List<Order> orders = dslJson.deserializeList(Order.class, lengthAware.body());

        if (orders != null) {
            Collections.sort(orders);

            // Remove duplicates
            Set<List<Integer>> seen = new HashSet<>();
            orders.removeIf(order -> !seen.add(Arrays.asList(order.region, order.atmId)));
        } else {
            orders = new ArrayList<>();
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
        dslJson.serialize(orders.stream().map(OrderResponse::new).collect(Collectors.toList()), os);

        return new RsWithType(new RsText(os.toByteArray()), "application/json");
    }
}
