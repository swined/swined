package org.swined.gae.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TheController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/index")
    public ModelAndView getIndex() {
        return new ModelAndView("index") {{
            addObject("hello", helloService.getHello());
        }};
    }

}
