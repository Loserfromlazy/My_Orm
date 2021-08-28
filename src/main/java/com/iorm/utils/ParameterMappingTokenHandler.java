package com.iorm.utils;

import java.util.ArrayList;
import java.util.List;


public class ParameterMappingTokenHandler implements TokenHandler {
	private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

	// content是参数名称 就是#{id} #{username}里面的id和username

	public String handleToken(String content) {
		parameterMappings.add(buildParameterMapping(content));
		return "?";
	}

	private ParameterMapping buildParameterMapping(String content) {
		ParameterMapping parameterMapping = new ParameterMapping(content);
		return parameterMapping;
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<ParameterMapping> parameterMappings) {
		this.parameterMappings = parameterMappings;
	}

}
