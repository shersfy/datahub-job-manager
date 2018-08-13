package org.shersfy.datahub.jobmanager.test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.shersfy.datahub.commons.utils.HttpUtil;
import org.shersfy.datahub.commons.utils.HttpUtil.HttpParamsLoader;
import org.shersfy.datahub.commons.utils.HttpUtil.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
    Logger logger = LoggerFactory.getLogger(getClass());

    private String path;

    @Before
    public void beforeMethod() throws IOException {
        logger.info("start ...");
        path = new File("tests/").getCanonicalPath()+File.separatorChar;
        logger.info("test files path={}", path);
    }

    @After
    public void afterMethod() {
        logger.info("finished");
    }
    
    public HttpResult testCase(String test) {
        HttpParamsLoader meta = new HttpParamsLoader(path+test);
        HttpResult res = HttpUtil.send(meta.getUrl(), meta.getMethod(), meta.getParams(), meta.getHeader());
        logger.info(res.toString());
        return res;
    }
}
