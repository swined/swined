package org.swined.gae.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TheController {

    @RequestMapping("/index.html")
    public ModelAndView getIndex() {
        throw new UnsupportedOperationException();
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView exceptionHandler(final Throwable throwable) {
        return new ModelAndView("error") {{
            addObject("error", throwable);
        }};
    }

}
