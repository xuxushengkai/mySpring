package com.sjj.spring.formework.webmvc.servlet;

import com.sjj.spring.formework.webmvc.annotation.SJController;
import com.sjj.spring.formework.webmvc.annotation.SJRequestMapping;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Servlet 只是作为一个 MVC 的启动入口
@Slf4j
public class SJDispatcherServlet extends HttpServlet {
    private final String LOCATION = "contextConfigLocation";

    //课后再去思考一下这样设计的经典之处
    // GPHandlerMapping 最核心的设计，也是最经典的
    // 它牛 B 到直接干掉了 Struts、Webwork 等 MVC 框架
    private List<SJHandlerMapping> handlerMappings = new ArrayList<SJHandlerMapping>();
    private Map<SJHandlerMapping, SJHandlerAdapter> handlerAdapters = new HashMap<SJHandlerMapping, SJHandlerAdapter>();

    private List<SJViewResolver> viewResolvers = new ArrayList<SJViewResolver>();

    private SJApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把 IOC 容器初始化了
        context = new SJApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(SJApplicationContext context) {
        //有九种策略
        // 针对于每个用户请求，都会经过一些处理的策略之后，最终才能有结果输出
        // 每种策略可以自定义干预，但是最终的结果都是一致
        // ============= 这里说的就是传说中的九大组件 ================
        initMultipartResolver(context); //文件上传解析，如果请求类型是 multipart 将通过 MultipartResolver 进行文件上传解析
        initLocalResolver(context);//本地化解析
        initThemeResolver(context);//主题解析
        /** 我们自己会实现 */
        //GPHandlerMapping 用来保存 Controller 中配置的 RequestMapping 和 Method 的一个对应关系
        initHandlerMappings(context);//通过handlermapping，将请求映射到处理器
        /** 我们自己会实现 */
        //HandlerAdapters 用来动态匹配 Method 参数，包括类转换，动态赋值
        initHandlerAdapters(context);//通过HandlerAdapter进行多类型的参数动态匹配

        initHandlerExceptionResolvers(context);//如果执行过程中遇到异常，将交个HandlerExceptionResolver 来解析
        initRequestToViewNameTranslator(context);//直接解析请求到视图名

        /** 我们自己会实现 */
        //通过 ViewResolvers 实现动态模板的解析
        // 自己解析一套模板语言
        initViewResolvers(context);//通过 viewResolver 解析逻辑视图到具体视图实现

        initFlashMapManager(context);//flash 映射管理器

    }


    private void initFlashMapManager(SJApplicationContext context) { }

    private void initRequestToViewNameTranslator(SJApplicationContext context) { }

    private void initHandlerExceptionResolvers(SJApplicationContext context) { }

    private void initThemeResolver(SJApplicationContext context) { }

    private void initLocalResolver(SJApplicationContext context) { }

    private void initMultipartResolver(SJApplicationContext context) { }



    //将 Controller 中配置的 RequestMapping 和 Method 进行一一对应
    private void initHandlerMappings(SJApplicationContext context) {
        //按照我们通常的理解应该是一个 Map
        // Map<String,Method> map;
        // map.put(url,Method)

        //首先从容器中取到所有的实例
        String[] beanNames = context.getBeanDefinitionNames();
        try{
            for(String beanName : beanNames){
                //到了 MVC 层，对外提供的方法只有一个 getBean 方法
                //返回的对象不是 BeanWrapper，怎么办？
                Object controller = context.getBean(beanName);
                //Object object = SJAouUtils.getTargetObject(proxy);
                Class<?> clazz = controller.getClass();

                if(!clazz.isAnnotationPresent(SJController.class)){
                    //只注入url和controller的关系
                    continue;
                }
                String baseUrl = "";

                if(clazz.isAnnotationPresent(SJRequestMapping.class)){
                    //拿到requestMapping的url
                    SJRequestMapping requestMapping = clazz.getAnnotation(SJRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                //扫描所有的 public 方法
                Method[] methods = clazz.getMethods();
                for(Method method:methods){
                    if(!method.isAnnotationPresent(SJRequestMapping.class)){
                        //再次判断是否是requestMapping修饰的method方法
                        continue;
                    }

                    SJRequestMapping requestMapping = method.getAnnotation(SJRequestMapping.class);
                    String regex = ("/"+baseUrl+requestMapping.value().replaceAll("\\*",".*")).replace("/*","/");
                    Pattern pattern = Pattern.compile(regex);
                    //加入handlermapping中
                    this.handlerMappings.add(new SJHandlerMapping(pattern,controller,method));
                    log.info("Mapping: " + regex + " , " + method);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initHandlerAdapters(SJApplicationContext context) {
        //在初始化阶段，我们能做的就是，将这些参数的名字或者类型按一定的顺序保存下来
        //因为后面用反射调用的时候，传的形参是一个数组
        //可以通过记录这些参数的位置 index,挨个从数组中填值，这样的话，就和参数的顺序无关了
        for(SJHandlerMapping handlerMapping: this.handlerMappings){
            //每一个方法有一个参数里列表，那么之类保存的是形参里列表
            this.handlerAdapters.put(handlerMapping,new SJHandlerAdapter());
        }
    }

    private void initViewResolvers(SJApplicationContext context) {
        //在页面敲一个 http://localhost/first.html
        //解决页面名字和模板文件关联的问题
        String templateRoot =  context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        File templateRootDir = new File(templateRoot);

        for(File template : templateRootDir.listFiles()){
            //遍历文件放入视图转换器中
            this.viewResolvers.add(new SJViewResolver(templateRoot));
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            //转发
            doDipatrh(req,resp);
        }catch (Exception e){
            resp.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>" + Arrays.toString(e.getStackTrace()
            ).replaceAll("\\[|\\]","").replaceAll("\\s","\r\n") + "<font color='green'><i>Copyright@GupaoEDU</i></font>");
            e.printStackTrace();
        }


    }

    private void doDipatrh(HttpServletRequest req, HttpServletResponse resp) throws  Exception{

        //根据用户请求的URL来获得一个Handler
        SJHandlerMapping handler = getHandler(req);
        if(handler == null){
            processDispatchResult(req,resp,new SJModelAndView("404"));
            return;
        }
        //根据handler获取adapter
        SJHandlerAdapter ha = getHandlerAdapter(handler);

        //这一步只是调用方法，得到返回值
        SJModelAndView mv = ha.handle(req,resp,handler);

        //这一步才是真的输出
        processDispatchResult(req,resp,mv);

    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, SJModelAndView mv) throws Exception {
        //调用 viewResolver 的 resolveView 方法
        if(null == mv){return;}

        if(this.viewResolvers.isEmpty()){return;}

        if(this.viewResolvers != null){
            for(SJViewResolver viewResolver : this.viewResolvers){
                //根据视图名称转换为视图
                SJView view = viewResolver.resolveViewName(mv.getViewName(),null);
                if(view != null){
                    view.render(mv.getModel(),req,resp);
                    return;
                }
            }
        }
    }

    //获取视图适配器
    private SJHandlerAdapter getHandlerAdapter(SJHandlerMapping handler) {
        if(this.handlerAdapters.isEmpty()){return null;}
        SJHandlerAdapter ha = this.handlerAdapters.get(handler);
        if(ha.supports(handler)){
            return ha;
        }
        return null;
    }

    private SJHandlerMapping getHandler(HttpServletRequest req) {
        if(this.handlerMappings.isEmpty()){return null;}

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();

        //转换路径
        url = url.replace(contextPath,"").replaceAll("/+","/");
        for(SJHandlerMapping handler: this.handlerMappings){
            Matcher matcher = handler.getpattern.matcher(url);
            if(!matcher.matches()){continue;}
            return handler;
        }
        return null;
    }

}
