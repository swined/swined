package org.swined.gae.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BetFairWeb {

    public static Set<String> getInPlayMarkets() throws IOException {
        return new HashSet<String>() {{
            String html = Utils.httpGet("http://www.betfair.com/exchange/football/coupon?id=4&goingInPlay=true");
            Matcher matcher = Pattern.compile("data-rules-marketId=\"([0-9]+\\.[0-9+])\"").matcher(html);
            while (matcher.find())
                add(matcher.group());
        }};
    }

}
