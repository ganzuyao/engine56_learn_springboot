package com.engine56.consumer.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;
import com.engine56.glue.common.response.JsonResponse;
import com.engine56.glue.order.OrderService;

@RestController
@RequestMapping(value="/")
public class HelloController {
	
	
	@Reference
	OrderService orderService;
	
	@RequestMapping(value="/order", method=RequestMethod.GET)
	public Object order(){
		JsonResponse<?> response = orderService.getAllOrder();
		System.out.println("[Dubbo服务消费端:]   远程调用 OrderService.allOrder()  ");
		return response;
	}
	
	@RequestMapping(value="/egError", method=RequestMethod.GET)
	public Object error(){
		return "[找不到对应的请求资源:]     404!!!!!!!!";
	}
	
	@ApiOperation(value="登录认证", notes="登录认证")
	@ApiImplicitParams({
		 @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true, dataType = "String"),
		 @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true, dataType = "String")
	 })
	@RequestMapping(value="/auth", method=RequestMethod.GET)
	public Object auth(@RequestParam String username,@RequestParam String password){
		String message = "登录成功";
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("username", username);
            //在session中保存当前用户的个人信息
        } catch (IllegalArgumentException e) {
            message="参数异常";
        } catch (AuthenticationException e) {
        	 message="认证失败";
        } catch (Exception e) {
        	 message="登录异常";
        }
        return message;
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public ModelAndView  logout(){
		ModelAndView view = new ModelAndView();
		SecurityUtils.getSubject().logout();
	    view.setViewName("redirect:http://127.0.0.1:8080/cas-server-webapp-4.2.1/logout?service=https://www.baidu.com");
		return view;
	}

}
