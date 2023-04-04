package com.pomirski.transactions;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;
import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@CompiledJson
public class AccountBalance {
    public static class DoubleConverter {
        public static double read(JsonReader<Double> reader) throws IOException {
            throw new IOException("This converter is only for writing");
        }
        public static void write(JsonWriter writer, double value) {
            writer.writeAscii(String.format(Locale.US, "%.2f", value));
        }
    }

    public String account;
    public int debitCount;
    public int creditCount;
    @JsonAttribute(converter = DoubleConverter.class)
    public double balance;

    public AccountBalance() {
    }

    public AccountBalance(String account) {
        this.account = account;
    }
}
