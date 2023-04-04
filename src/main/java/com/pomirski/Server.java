package com.pomirski;

import com.pomirski.atm.AtmApp;
import com.pomirski.transactions.TransactionsApp;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public class Server {
    public static void main(final String... args) throws Exception {
        new FtBasic(
                new TkFork(
                        new FkRegex("/atms/calculateOrder", new AtmApp()),
                        new FkRegex("/transactions/report", new TransactionsApp())),
                8080
        ).start(Exit.NEVER);
    }
}
