package com.sjj.spring.formework.webmvc.servlet;

import com.sjj.spring.formework.aop.SJAopConfig;
import com.sjj.spring.formework.aop.SJAopProxy;
import com.sjj.spring.formework.aop.SJCGlibAopProxy;
import com.sjj.spring.formework.aop.SJJdkDynamicAopProxy;
import com.sjj.spring.formework.aop.support.SJAdvisedSupport;
import com.sjj.spring.formework.webmvc.annotation.SJAutoWired;
import com.sjj.spring.formework.webmvc.annotation.SJController;
import com.sjj.spring.formework.webmvc.annotation.SJService;
import com.sjj.spring.formework.webmvc.beans.config.SJBeanDefinition;
import com.sjj.spring.formework.webmvc.beans.config.SJBeanDefinitionReader;
import com.sjj.spring.formework.webmvc.beans.config.SJBeanWrapper;
import com.sjj.spring.formework.webmvc.beans.context.SJBeanPostProcessor;
import com.sjj.spring.formework.webmvc.beans.support.SJDefaultListableBeanFactory;
import com.sjj.spring.formework.webmvc.core.SJBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName SJApplicationContext
 * @Description TODO
 * Author shengjunjie
 * Date 2019/10/22 11:57
 **/
public class SJApplicationContext extends SJDefaultListableBeanFactory implements SJBeanFactory {

    private String[] configLocations;
    private SJBeanDefinitionReader reader;

    //单例的IOC容器缓存
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();
    //通用的IOC容器
    private Map<String, SJBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, SJBeanWrapper>();


    //定位路径
    public SJApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //1、定位配置文件位置
        reader = new SJBeanDefinitionReader(this.configLocations);

        //2、加载配置文件，扫描相关的类，把他们封装成BeanDefinition
        List<SJBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        //3、注册，把注册信息放到容器里面(伪IOC容器)
        doRegisterBeanDefinitions(beanDefinitions);

        //4、把不是延时加载的类，提前初始化
        doAutowired();
    }

    //只处理非延时加载的情况
    private void doAutowired() {
        for (Map.Entry<String, SJBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            //拿到类名
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    //根据类型获取对应的类
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //注册类
    private void doRegisterBeanDefinitions(List<SJBeanDefinition> beanDefinitions) throws Exception {
        for (SJBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                //已注册对应的类，抛出异常
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }
    //到这里为止，容器初始化完毕

    @Override
    public Object getBean(String beanName) throws Exception {

        SJBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try {
            //生成通知时间
            SJBeanPostProcessor beanPostProcessor = new SJBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            //在实例初始化以前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            SJBeanWrapper beanWrapper = new SJBeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName, beanWrapper);
            //在实例初始化以后调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            populateBean(beanName, instance);
            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        //不是所有牛奶都叫特仑苏
        if (!(clazz.isAnnotationPresent(SJController.class) || (clazz.isAnnotationPresent(SJService.class)))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        //遍历注入
        for (Field field : fields) {
            if (!field.isAnnotationPresent(SJAutoWired.class)) {
                continue;
            }
            SJAutoWired autoWired = field.getAnnotation(SJAutoWired.class);
            String autowiredBeanName = autoWired.value().trim();
            if ("".equals(autowiredBeanName)) {
                //注入名称为空
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                //为什么会为NULL，先留个坑
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    //传一个 BeanDefinition，就返回一个实例 Bean
    private Object instantiateBean(SJBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        //因为根据 Class 才能确定一个类是否有实例
        System.out.println("classname ==" +className);
        try {
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanInstanceCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();

                SJAdvisedSupport config = instantionAopConfig(beanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);
                System.out.println("config=" + config);

                if (config.pointCutmatch()) {
                    instance = createProxy(config).getProxy();
                }

                System.out.println("instance == "+instance);

                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private SJAopProxy createProxy(SJAdvisedSupport config) {
        Class targetClass = config.getTargetClass();
        if(targetClass.getInterfaces().length > 0){
            //加载配置到jdk代理中
            return new SJJdkDynamicAopProxy(config);
        }
        return new SJCGlibAopProxy(config);
    }

    //加入切面信息
    private SJAdvisedSupport instantionAopConfig(SJBeanDefinition beanDefinition) {

        SJAopConfig config = new SJAopConfig();
        config.setPointCut(reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new SJAdvisedSupport(config);
    }

    //依赖注入，从这里开始，通过读取 BeanDefinition 中的信息
    // 然后，通过反射机制创建一个实例并返回
    // Spring 做法是，不会把最原始的对象放出去，会用一个 BeanWrapper 来进行一次包装
    // 装饰器模式：
    // 1、保留原来的 OOP 关系
    // 2、我需要对它进行扩展，增强（为了以后 AOP 打基础）
    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    //获取长度
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    //获取配置
    public Properties getConfig() {
        return this.reader.getConfig();
    }
}

