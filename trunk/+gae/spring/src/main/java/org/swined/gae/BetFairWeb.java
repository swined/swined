package org.swined.gae;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BetFairWeb {

    public static Set<String> getLiveSoccerMarkets() throws IOException {
        return new HashSet<String>() {{
            String html = Utils.httpGet("http://www.betfair.com/exchange/football/coupon?id=4&goingInPlay=true");
            while (html.contains("data-rules-marketId=\"")) {
                html = html.split("data-rules-marketId=\"", 2)[1];
                add(html.split("\"")[0]);
            }
        }};
    }

}
