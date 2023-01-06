package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class DSLParamStrategy extends DSLMethodStrategy {

	private final String methodName;

	public DSLParamStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
		super(tabs, propertyDescriptor, methodName);
		this.methodName = methodName;
	}

	@Override
	public String toDSL() {
		return replaceTabs(String.format(getSyntax("syntax.method_call"),
				methodName, getOrderedChildrenDSL()), getTabs());
	}

	public String getOrderedChildrenDSL() {
		PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
		if (propertyDescriptor.getName().equals("hudson.model.StringParameterDefinition") || propertyDescriptor.getName().equals("hudson.model.BooleanParameterDefinition")) {
			String defaultValue = "\"\"";
			String description = "\"\"";
			List <PropertyDescriptor> children = propertyDescriptor.getProperties();
			for (PropertyDescriptor child : children ) {
				if (child.getName().equals("defaultValue")) {
					defaultValue = getChildrenByName("defaultValue").toDSL();
				}
				if (child.getName().equals("description")) {
					description = getChildrenByName("description").toDSL();
				}
			}
			String name = getChildrenByName("name").toDSL();
			return name + ", " + defaultValue + ", " + description;

		} else {
			String variable = getChildrenByName("variable").toDSL();
			String credentialsId = getChildrenByName("credentialsId").toDSL();
			return variable + ", " + credentialsId;
		}
	}
}
