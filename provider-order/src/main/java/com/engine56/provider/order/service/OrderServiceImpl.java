package com.engine56.provider.order.service;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.engine56.glue.common.response.JsonResponse;
import com.engine56.glue.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public  JsonResponse<List<String>> getAllOrder() {
		List<String> orderList = new ArrayList<String>();
		orderList.add("八宝鸭");
		orderList.add("脆皮鸡");
		orderList.add("香辣蟹");
		orderList.add("椒盐鸭下巴");
		JsonResponse<List<String>> response = JsonResponse.newSuccess(orderList);
		return response;
	}

}
