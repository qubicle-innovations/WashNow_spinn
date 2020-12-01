package com.spinwash.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPojo {
	
	public  List<OrderVo> info;

}
