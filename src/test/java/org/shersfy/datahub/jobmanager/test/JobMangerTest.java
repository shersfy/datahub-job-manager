package org.shersfy.datahub.jobmanager.test;

import java.io.IOException;

import org.junit.Test;


public class JobMangerTest extends BaseTest{
    
    
    @Test
    public void test01() {
        String test = "test1.txt";
        testCase(test);
    }
    
    @Test
    public void test02() {
        String test = "test2.txt";
        testCase(test);
    }
    
    @Test
    public void test03() throws IOException {
        String test = "test3.txt";
        testCase(test);
    }
    
}
