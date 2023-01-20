package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLCleanupPatternStrategy extends DSLObjectStrategy {

    private final String name;

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        System.out.println(children);
        List<PropertyDescriptor> renamedChildren = new ArrayList<>();
        PropertyDescriptor parent = propertyDescriptor.getParent();
        System.out.println(parent);

        PropertyDescriptor typeChild = null;
        PropertyDescriptor patternChild = null;
        for (PropertyDescriptor child : children) {
            if (child.getName().equals("type")) {
               typeChild = child;
            }
            if (child.getName().equals("pattern")) {
                patternChild = child;
            }
        }
//
        if (typeChild.getValue().equals("INCLUDE")) {
            PropertyDescriptor newChild = new PropertyDescriptor(String.format("includePattern"),
                    parent.getParent(),
                    patternChild.getValue());

           parent.addProperty(newChild);

        } else if (typeChild.getValue().equals("EXCLUDE")) {
            PropertyDescriptor newChild = new PropertyDescriptor(String.format("excludePattern"),
                    parent.getParent(),
                    patternChild.getValue());

            parent.addProperty(newChild);
        }

//
//        PropertyDescriptor newChild = new PropertyDescriptor(
//                String.format("syntax.method_call", typeChild),
////                child.getParent(),
//                propertyDescriptor.getParent(),
//                typeChild.getValue(),
//                typeChild.getProperties(),
//                typeChild.getAttributes());
//
//        initChildren(newChild);
    }
//
//        List<PropertyDescriptor> patternDescriptors = new ArrayList<>();

        // get the relevant children
//        for (PropertyDescriptor child : children) {
//           if (child.getName().equals("patterns")) {
//               patternDescriptors.add(child);
//           }
//        }
//
//        PropertyDescriptor typeChild;
//        for (PropertyDescriptor child : patternDescriptors) {
////            PgetPropertyByName("type");
//        }
//    }
    public String toDSL() {
        String childrenDSL = getChildrenDSL();
//        System.out.println(childrenDSL);
        if (!childrenDSL.isEmpty()) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}
