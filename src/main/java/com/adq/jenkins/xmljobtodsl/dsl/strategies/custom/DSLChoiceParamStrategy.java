package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;

import java.util.List;

public class DSLChoiceParamStrategy extends DSLMethodStrategy {

	private final String methodName;

	public DSLChoiceParamStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
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
		String description = "\"\"";

		List<PropertyDescriptor> children = propertyDescriptor.getProperties();
		for (PropertyDescriptor child : children ) {
			if (child.getName().equals("defaultValue")) {
				description = getChildrenByName("description").toDSL();

			}
		}
		String choices = getChildrenByName("choices").toDSL();
		String name = getChildrenByName("name").toDSL();
		return name + ", " + choices  + ", " + description;
	}
}
