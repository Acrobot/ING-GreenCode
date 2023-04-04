package com.pomirski.transactions;

import com.dslplatform.json.CompiledJson;

import java.math.BigDecimal;

@CompiledJson
public class Transaction {
    public String debitAccount;
    public String creditAccount;

    /**
     * We should be storing money in a better data format, but our input is a JSON number,
     * not a String, so we do not have a choice here since almost all JSON parsers default to double anyway.
     */
    public double amount;
}
