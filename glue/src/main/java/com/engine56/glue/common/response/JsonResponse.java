package com.engine56.glue.common.response;

import java.io.Serializable;

public class JsonResponse<T extends Object> implements Serializable {

	private static final long serialVersionUID = 3448739161006295908L;
	
	/**
	 * 返回数据
	 */
	private T data;
	
	/**
	 * 响应状态: 0:成功  1:失败   
	 */
	private int status;
	
	/**
	 * 响应信息
	 */
	private String message;
	
	/**
	 * 创建成功的JsonResponse实例,无返回数据
	 * 响应状态:  status   默认: 0
	 * 响应信息:  message  默认: success
	 * @return JsonResponse实例
	 */
	public static <T> JsonResponse<T> newSuccess(){
		JsonResponse<T> jsonResponse = new JsonResponse<T>();
		jsonResponse.setStatus(ResponseEnum.SUCCESS.getStatus());
		jsonResponse.setMessage(ResponseEnum.SUCCESS.getMessage());
		return jsonResponse;
	}
	
	/**
	 * 创建成功的JsonResponse实例,有返回数据
	 * 响应状态:  status   默认: 0
	 * 响应信息:  message  默认: success
	 * @param data 返回数据
	 * @return JsonResponse实例
	 */
	public static <T> JsonResponse<T> newSuccess(T data){
		JsonResponse<T> jsonResponse = new JsonResponse<T>();
		jsonResponse.setData(data);
		jsonResponse.setStatus(ResponseEnum.SUCCESS.getStatus());
		jsonResponse.setMessage(ResponseEnum.SUCCESS.getMessage());
		return jsonResponse;
	}
	
	/**
	 * 创建成功的JsonResponse实例,有返回数据和返回信息
	 * 响应状态:  status   默认: 0
	 * @param data 返回数据
	 * @param successMsg 响应信息
	 * @return JsonResponse实例
	 */
	public static <T> JsonResponse<T> newSuccess(T data,String successMsg){
		JsonResponse<T> jsonResponse = new JsonResponse<T>();
		jsonResponse.setData(data);
		jsonResponse.setStatus(ResponseEnum.SUCCESS.getStatus());
		jsonResponse.setMessage(successMsg);
		return jsonResponse;
	}
	
	/**
	 * 创建失败的JsonResponse实例,无返回对象
	 * 响应状态:  status   默认: 1
	 * 响应信息:  message  默认: error
	 * @return JsonResponse实例
	 */
	public static <T> JsonResponse<T> newFailure(){
		JsonResponse<T> jsonResponse = new JsonResponse<T>();
		jsonResponse.setStatus(ResponseEnum.ERROR.getStatus());
		jsonResponse.setMessage(ResponseEnum.ERROR.getMessage());
		return jsonResponse;
	}
	
	/**
	 * 创建失败的JsonResponse实例,无返回对象
	 * 响应状态:  status   默认: 1
	 * @param  errorMsg 响应信息
	 * @return JsonResponse实例
	 */
	public static <T> JsonResponse<T> newFailure(String errorMsg){
		JsonResponse<T> jsonResponse = new JsonResponse<T>();
		jsonResponse.setStatus(ResponseEnum.ERROR.getStatus());
		jsonResponse.setMessage(errorMsg);
		return jsonResponse;
	}
	
	public JsonResponse() {}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
