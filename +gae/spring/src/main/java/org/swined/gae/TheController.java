package org.swined.gae;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.swined.gae.BetFairWeb;

import javax.servlet.ServletResponse;
import java.io.IOException;

@Controller
public class TheController {

    @RequestMapping("/index.html")
    public void getIndex() {
        throw new UnsupportedOperationException();
    }

    @RequestMapping("/bf/inPlayMarkets.html")
    public void getInPlayMarkets(ServletResponse response) throws IOException {
        response.getWriter().write(BetFairWeb.getInPlayMarkets().toString());
    }

    @ExceptionHandler(Throwable.class)
    public void exceptionHandler(Throwable throwable, ServletResponse response) throws IOException {
        response.getWriter().write("<pre>");
        throwable.printStackTrace(response.getWriter());
        response.getWriter().write("</pre>");
    }

}
