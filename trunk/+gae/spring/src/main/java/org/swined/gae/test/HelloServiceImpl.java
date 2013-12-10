package org.swined.gae.test;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String getHello() {
        return "goodbye, world";
    }

}
