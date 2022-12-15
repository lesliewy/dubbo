package cn.wy.test1;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestProvider1 {
    public static void main(String[] args) throws Exception {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
//        String config = WyTest1.class.getPackage().getName().replace('.', '/') + "/WyTest1.xml";
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/home/leslie/myprojects/GitHub/dubbo/dubbo-test/wy/src/main/resources/WyTest1.xml"});
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("file:///home/leslie/myprojects/GitHub/dubbo/dubbo-test/wy/src/main/resources/test1/wy-provider1.xml");
        context.start();
        System.in.read();
    }
}
