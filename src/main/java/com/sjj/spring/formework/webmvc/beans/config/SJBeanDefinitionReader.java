package com.sjj.spring.formework.webmvc.beans.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName SJBeanDefinitionReader
 * @Description 用对配置文件进行查找，读取、解析
 * Author shengjunjie
 * Date 2019/10/22 13:55
 **/
public class SJBeanDefinitionReader {

    //注册类
    private List<String> registryBeanClasses = new ArrayList<String>();

    //配置对象
    private Properties config = new Properties();

    //固定配置文件中的key,相对于xml的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public SJBeanDefinitionReader(String... locations) {
        //通过URL定位找到其所对应的文件，然后转为文件流
        InputStream is = this.getClass().getClassLoader().
                getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/就 OK 了
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        //循环替换
        for(File file : classPath.listFiles()){
            if(file.isDirectory()){
                //如果有目录
                doScanner(scanPackage + "." + file.getName());
            }else{
                //如果不是class文件
                if(!file.getName().endsWith(".class")){continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registryBeanClasses.add(className);
            }
        }
    }

    //加载类配置
    public List<SJBeanDefinition> loadBeanDefinitions() {
        List<SJBeanDefinition> result = new ArrayList<SJBeanDefinition>();
        try{
            for(String className: registryBeanClasses){
                //反射
                Class<?> beanClass = Class.forName(className);
                //如果是接口
                if(beanClass.isInterface()){continue;}
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
                Class<?>[] interfaces = beanClass.getInterfaces();
                for(Class<?> i:interfaces){
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //把每一个配信息解析成一个 BeanDefinition
    private SJBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        SJBeanDefinition beanDefinition = new SJBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    //如果类名本身是小写字母，确实会出问题
    // 但是我要说明的是：这个方法是我自己用，private 的
    // 传值也是自己传，类也都遵循了驼峰命名法
    // 默认传入的值，存在首字母小写的情况，也不可能出现非字母的情况
    // 为了简化程序逻辑，就不做其他判断了，大家了解就 OK
    // 其实用写注释的时间都能够把逻辑写完了
    private String toLowerFirstCase(String simpleName){
        char[] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的 ASCII 码相差 32，
        // 而且大写字母的 ASCII 码要小于小写字母的 ASCII 码
        // 在 Java 中，对 char 做算学运算，实际上就是对 ASCII 码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }

    //获取配置
    public Properties getConfig() {
        return this.config;
    }
}
