package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DSLParameterStrategy extends AbstractDSLStrategy implements IValueStrategy {

    public DSLParameterStrategy(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
    }

    @Override
    public String toDSL() {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        if (propertyDescriptor.getParent().getName().equals("hudson.model.StringParameterDefinition") && propertyDescriptor.getName().equals("defaultValue")
                || propertyDescriptor.getParent().getName().equals("a")) {
            return printIntAsString(((PropertyDescriptor) getDescriptor()).getValue());
        }

//        if (propertyDescriptor.getParent().getName().equals("a")) {
//            return printIntAsString(((PropertyDescriptor) getDescriptor()).getValue());
//        }

        return printValueAccordingOfItsType(((PropertyDescriptor) getDescriptor()).getValue());
    }

    public String printIntAsString(String value) {
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
