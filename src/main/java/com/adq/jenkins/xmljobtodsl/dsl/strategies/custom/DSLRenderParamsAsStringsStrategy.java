package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLParameterStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSLRenderParamsAsStringsStrategy extends DSLParameterStrategy {

    private final String name;

    public DSLRenderParamsAsStringsStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
        this.name = "";
    }

    public DSLRenderParamsAsStringsStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(propertyDescriptor);
        this.name = name;
    }

    @Override
    public String toDSL() {
        return printValueAccordingOfItsType(((PropertyDescriptor) getDescriptor()).getValue());
    }

    @Override
    public String printValueAccordingOfItsType(String value) {
        if (value == null) {
            return "\"\"";
        }
        if (value.isEmpty()) {
            return "\"\"";
        }

        value = value.replaceAll("\\\\", "\\\\\\\\");
        value = value.replaceAll("\\$", Matcher.quoteReplacement("\\$"));

        if (value.contains("\n")) {
            value = value.replaceAll(Pattern.quote("\"\"\""), Matcher.quoteReplacement("\\\"\\\"\\\""));
            return "\"\"\"" + value + "\"\"\"";
        } else {
            value = value.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("\\\""));
        }
        return "\"" + value + "\"";
    }
}
