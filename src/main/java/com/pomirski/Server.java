package com.pomirski;

import com.pomirski.atm.AtmApp;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;

public class Server {
    public static void main(final String... args) throws Exception {
        new FtBasic(
                new TkFork(new FkRegex("/atms/calculateOrder", new AtmApp())), 8080
        ).start(Exit.NEVER);
    }
}
