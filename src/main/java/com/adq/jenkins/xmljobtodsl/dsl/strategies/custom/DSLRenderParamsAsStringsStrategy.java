package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLMethodStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSLRenderParamsAsStringsStrategy extends DSLMethodStrategy {

    private final String methodName;

    public DSLRenderParamsAsStringsStrategy(int tabs, PropertyDescriptor propertyDescriptor, String methodName) {
        super(tabs, propertyDescriptor, methodName);
        this.methodName = methodName;
    }

    @Override
    public String toDSL() {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();

        return replaceTabs(String.format(getSyntax("syntax.method_call"),
                methodName, printValueAccordingOfItsType(propertyDescriptor.getValue())), getTabs());
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
