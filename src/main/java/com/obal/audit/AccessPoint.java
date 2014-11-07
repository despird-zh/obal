
package com.obal.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * AccessPoint is the client information that launch operation
 * 
 * @author despird
 * @version 0.1 2014-2-1
 * 
 **/
public class AccessPoint {
	
	private String name;
	private String ipAddress;

	/**
	 * Constructor with name
	 * 
	 *  @param name the access point name
	 **/
	public AccessPoint(String name) {
		this.name = name;
		try {
			InetAddress iaddr =  InetAddress.getLocalHost();
			this.ipAddress = iaddr.getHostAddress();
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Constructor with name and ipaddress
	 * 
	 * @param name the access point name
	 * @param ipaddress the ip address 
	 **/
	public AccessPoint(String name, String ipAddress) {
		this.name = name;
		this.ipAddress = ipAddress;
	}

	/**
	 * Get IP address
	 * 
	 *  @return String the ip address
	 **/
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Get name 
	 * 
	 * @return String the name
	 **/
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		
		return new HashCodeBuilder(17, 37).append(name)
				.append(ipAddress).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AccessPoint other = (AccessPoint) obj;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return new EqualsBuilder()
		.append(this.name, other.name)
		.append(this.ipAddress, other.ipAddress).isEquals();
	}

	@Override
	public String toString() {
		String retValue = "";

		retValue = "App(" + "name=" + this.name + ", ip=" + this.ipAddress
				+ ")";

		return retValue;
	}
}
