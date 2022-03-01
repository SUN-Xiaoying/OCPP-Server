package com.xiao.csms.impl;

import com.xiao.csms.server.CentralSystem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class CentralSystemImpl {
    private final CentralSystem centralSystem;

    @PostConstruct
    public void startServer() throws Exception {
        centralSystem.start();
    }

}
