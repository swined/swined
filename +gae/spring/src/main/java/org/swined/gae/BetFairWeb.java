package org.swined.gae;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class BetFairWeb {

    public static Collection<String> getLiveSoccerMarkets() throws IOException {
        return new HashSet<String>() {{
            String html = Utils.httpGet("http://www.betfair.com/exchange/football/coupon?id=4&goingInPlay=true");
            while (html.contains("data-rules-marketId=\"")) {
                html = html.split("data-rules-marketId=\"", 2)[1];
                add(html.split("\"")[0]);
            }
        }};
    }

}
