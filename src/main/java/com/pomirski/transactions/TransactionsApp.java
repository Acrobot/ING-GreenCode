package com.pomirski.transactions;

import com.dslplatform.json.DslJson;
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

public class TransactionsApp implements Take {
    private static final int BUFFER_SIZE = 1024;
    private final DslJson<Object> dslJson;

    public TransactionsApp() {
        this.dslJson = new DslJson<>(Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());
    }

    @Override
    public Response act(Request request) throws Exception {
        Request lengthAware = new RqLengthAware(request);
        List<Transaction> transactions = dslJson.deserializeList(Transaction.class, lengthAware.body());

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        HashMap<String, AccountBalance> accountBalances = transactions.parallelStream().map(
                t -> Arrays.asList(
                    new MoneyChangeEvent(t.debitAccount, -t.amount),
                    new MoneyChangeEvent(t.creditAccount, t.amount)))
                .flatMap(List::parallelStream)
                .reduce(new HashMap<>(), (store, transaction) -> {
                    if (!store.containsKey(transaction.getAccountId())) {
                        store.put(transaction.getAccountId(), new AccountBalance(transaction.getAccountId()));
                    }
                    AccountBalance accountBalance = store.get(transaction.getAccountId());
                    accountBalance.balance += transaction.getMoneyChange();
                    if (transaction.getMoneyChange() > 0) {
                        accountBalance.creditCount += 1;
                    } else {
                        accountBalance.debitCount += 1;
                    }
                    return store;
                }, (store, otherMap) -> {
                    store.putAll(otherMap);
                    return store;
                });
        TreeMap<String, AccountBalance> balancesSorted = new TreeMap<>(accountBalances);

        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
        dslJson.serialize(balancesSorted.values(), os);

        return new RsWithType(new RsText(os.toByteArray()), "application/json");
    }
}
