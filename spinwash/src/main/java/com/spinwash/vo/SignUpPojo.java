package com.spinwash.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUpPojo implements Serializable {

	private static final long serialVersionUID = 1L;

	private OrderVo order;
	private String message;
	private UserVO user;
	public OrderVo getOrder() {
		return order;
	}
	public void setOrder(OrderVo order) {
		this.order = order;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public UserVO getUser() {
		return user;
	}
	public void setUser(UserVO user) {
		this.user = user;
	}
	

}
