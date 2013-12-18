package org.swined.gae;

import org.json.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletResponse;
import java.io.IOException;

@Controller
public class TheController {

    @RequestMapping("/index.html")
    public void getIndex() {
        throw new UnsupportedOperationException();
    }

    @ResponseBody
    @RequestMapping(
            value = "/bf/liveSoccerMarkets.json",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getLiveSoccerMarkets() throws IOException {
        return new JSONArray(BetFairWeb.getLiveSoccerMarkets()).toString();
    }

    @ExceptionHandler(Throwable.class)
    public void exceptionHandler(Throwable throwable, ServletResponse response) throws IOException {
        response.getWriter().write("<pre>");
        throwable.printStackTrace(response.getWriter());
        response.getWriter().write("</pre>");
    }

}
