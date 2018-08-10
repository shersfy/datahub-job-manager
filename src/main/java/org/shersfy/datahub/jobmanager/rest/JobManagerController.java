package org.shersfy.datahub.jobmanager.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobManagerController {
    
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @RequestMapping("/")
    public Object index() {
        return "Welcom Datahub Job Manager Application";
    }
}
