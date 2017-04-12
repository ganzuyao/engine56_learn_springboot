package com.engine56.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;

@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboConsumerConfiguration {
	
	@Value("${dubbo.application.name}")
	private String applicationName;

	@Value("${dubbo.application.logger}")
	private String logger;
	
	@Value("${dubbo.consumer.version}")
	private String version;
	
	@Value("${dubbo.registr.protocol}")
	private String protocol;

	@Value("${dubbo.registry.address}")
	private String registryAddress;
	
	
	/**
     * 注入dubbo上下文
     * 
     * @return
     */
    @Bean
    public ApplicationConfig applicationConfig() {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(this.applicationName);
        applicationConfig.setLogger(logger);
        return applicationConfig;
    }
    
    /**
     * 配置Dubbo消费端
     * 
     * @return
     */
    @Bean
    public ConsumerConfig consumerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig){
    	ConsumerConfig consumerConfig = new ConsumerConfig();
    	consumerConfig.setVersion(version);
    	consumerConfig.setApplication(applicationConfig);
    	consumerConfig.setRegistry(registryConfig);
    	return consumerConfig;
    	
    }
	
	/**
     * 设置dubbo扫描包
     * @param packageName
     * @return
     */
    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packageName) {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(packageName);
        return annotationBean;
    }
    
    /**
     * 注入dubbo注册中心配置,基于zookeeper
     * 
     * @return
     */
    @Bean
    public RegistryConfig registryConfig() {
        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol(protocol);
        registry.setAddress(registryAddress);
        return registry;
    }

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}
}
