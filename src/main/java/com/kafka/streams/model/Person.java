package com.kafka.streams.model;

import java.io.Serializable;

public class Person implements Serializable{
	private static final long serialVersionUID = 7788226434562901520L;
	//@JsonProperty
	private Long id;
	//@JsonProperty
	private String lastName;
	//@JsonProperty
	private String firstName;
	//@JsonProperty
	private long addressId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	
}
