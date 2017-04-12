package com.engine56.consumer.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.engine56.consumer.shiro.EgShiroCasRealm;

@Configuration
public class ShiroCasConfiguration {
	
	 private static final Logger logger = LoggerFactory.getLogger(ShiroCasConfiguration.class);
	
	// CasServerUrlPrefix
    public static final String casServerUrlPrefix = "http://127.0.0.1:8080/cas-server-webapp-4.2.1";
    // Cas登录页面地址
    public static final String casLoginUrl = casServerUrlPrefix + "/login";
    // Cas登出页面地址
    public static final String casLogoutUrl = casServerUrlPrefix + "/logout";
    // 当前工程对外提供的服务地址
    public static final String shiroServerUrlPrefix = "http://127.0.0.1:5700";
    // casFilter UrlPattern
    public static final String casFilterUrlPattern = "/shiro-cas";
    // 登录地址
    public static final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern+"?locale=zh_CN";
    
    @Bean
    public EhCacheManager getEhCacheManager() {  
        EhCacheManager em = new EhCacheManager();  
        em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
        return em;  
    }  
    
    @Bean(name = "egShiroCasRealm")
    public EgShiroCasRealm egShiroCasRealm(EhCacheManager cacheManager) {  
    	EgShiroCasRealm realm = new EgShiroCasRealm(); 
        realm.setCacheManager(cacheManager);
        return realm;
    } 
    
    /**
     * 注册DelegatingFilterProxy（Shiro）
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理  
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }
    
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }
    
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(EgShiroCasRealm egShiroCasRealm) {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(egShiroCasRealm);
//      <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 --> 
        dwsm.setCacheManager(getEhCacheManager());
        // 指定 SubjectFactory
        dwsm.setSubjectFactory(new CasSubjectFactory());
        return dwsm;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
    
    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        /////////////////////// 下面这些规则配置最好配置到配置文件中 ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");// shiro集成cas后，首先添加该规则
        // authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc.FormAuthenticationFilter
        // anon：它对应的过滤器里面是空的,什么都没做
        logger.info("##################从数据库读取权限规则，加载到shiroFilter中##################");
        filterChainDefinitionMap.put("/login", "authc,perms[role1:manager]");// 这里为了测试，固定写死的值，也可以从数据库或其他配置中读取
        filterChainDefinitionMap.put("/**", "anon");//anon 可以理解为不拦截
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
    
    /**
     * CAS过滤器
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
        casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面
        return casFilter;
    }

    /**
     * ShiroFilter<br/>
     * 然后读取数据库相关配置，配置到 shiroFilterFactoryBean 的访问规则中。实际项目中，请使用自己的Service来处理业务逻辑。
     *
     * @param myShiroCasRealm
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager  
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 登录成功后要跳转的连接
        shiroFilterFactoryBean.setSuccessUrl("/order");
        shiroFilterFactoryBean.setUnauthorizedUrl("/egError");
        // 添加casFilter到shiroFilter中
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("casFilter", casFilter);
        shiroFilterFactoryBean.setFilters(filters);
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }
}
