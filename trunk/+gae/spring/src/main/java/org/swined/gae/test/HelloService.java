package org.swined.gae.test;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String getHello() {
        return "goodbye, world";
    }

}
