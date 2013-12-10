package org.swined.gae.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class TheController {

    @Autowired
    final HelloService helloService = null;

    @RequestMapping("/index")
    public ModelAndView getIndex(
            final @RequestParam(required = false) String p,
            final HttpSession session
    ) {
        return new ModelAndView("index") {{
            addObject("hello", helloService.getHello());
            addObject("p", p);
            Integer c = (Integer) session.getAttribute("c");
            if (c == null)
                c = 0;
            else
                c++;
            session.setAttribute("c", c);
            addObject("c", c);
        }};
    }

}
