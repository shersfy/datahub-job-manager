package org.shersfy.datahub.jobmanager.test;

import org.junit.Test;
import org.shersfy.datahub.commons.utils.HttpUtil;
import org.shersfy.datahub.commons.utils.HttpUtil.HttpParamsLoader;
import org.shersfy.datahub.commons.utils.HttpUtil.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JobMangerTest {
    
    Logger logger = LoggerFactory.getLogger(getClass()); 
    
    @Test
    public void test01() {
        String test = "test01.txt";
        call(test);
    }
    @Test
    public void test02() {
        String test = "test02.txt";
        call(test);
    }
    
    
    public void call(String test) {
        HttpParamsLoader meta = new HttpParamsLoader(getClass().getResource(test).getPath());
        HttpResult res = HttpUtil.send(meta.getUrl(), meta.getMethod(), meta.getParams(), meta.getHeader());
        logger.info(res.toString());
    }
    
}
