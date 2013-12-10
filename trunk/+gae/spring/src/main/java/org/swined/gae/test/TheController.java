package org.swined.gae.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TheController {

    @Autowired
    final HelloService helloService = null;

    @RequestMapping("/index")
    public ModelAndView getIndex(final @RequestParam(required = false) String p) {
        return new ModelAndView("index") {{
            addObject("hello", helloService.getHello());
            addObject("p", p);
        }};
    }

}
