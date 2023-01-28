package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/*
Strategy for preBuildCleanup attribute, to correctly render includePattern and excludePattern, which is in the XML as:
<hudson.plugins.ws__cleanup.PreBuildCleanup plugin="ws-cleanup@0.43">
            <patterns>
                <hudson.plugins.ws__cleanup.Pattern>
                    <pattern>results/*</pattern>
                    <type>INCLUDE</type>
                </hudson.plugins.ws__cleanup.Pattern>
            </patterns>
            <deleteDirs>false</deleteDirs>
            <cleanupParameter>test</cleanupParameter>
        </hudson.plugins.ws__cleanup.PreBuildCleanup>

 The value within type determines the method name: either "includePattern" or "excludePattern"
 "pattern" is then inserted as the value for that method
  Returns the dsl as:
   preBuildCleanup {
			includePattern("test")
			deleteDirectories(false)
			cleanupParameter("test")
		}
 */

public class DSLCleanupPatternStrategy extends DSLObjectStrategy {

    PropertyDescriptor typeChild = null;
    PropertyDescriptor patternChild = null;


    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        List<PropertyDescriptor> leftoverProps = new ArrayList<>();
        List<PropertyDescriptor> patternProps = new ArrayList<>();

        separateProperties(children, leftoverProps, patternProps);
        extractProperties(propertyDescriptor.getProperties());
        processPatternProps(patternProps, leftoverProps, propertyDescriptor);
    }

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        List<PropertyDescriptor> leftoverProps = new ArrayList<>();
        List<PropertyDescriptor> patternProps = new ArrayList<>();

        separateProperties(children, leftoverProps, patternProps);
        extractProperties(patternProps);
        processPatternProps(patternProps, leftoverProps, propertyDescriptor);
    }

    private void separateProperties(List<PropertyDescriptor> children, List<PropertyDescriptor> leftoverProps, List<PropertyDescriptor> patternProps) {
        for (PropertyDescriptor child : children) {
            if (!child.getName().equals("patterns")) {
                leftoverProps.add(child);
            }
            if (child.getName().equals("patterns")) {
                patternProps.add(child);
            }
        }
    }

    private void extractProperties(List<PropertyDescriptor> patternProps) {
        for (PropertyDescriptor prop : patternProps) {
            if (prop.getName().equals("patterns")) {
                for (PropertyDescriptor innerProp : prop.getProperties()) {
                    for (PropertyDescriptor nestedProp : innerProp.getProperties()) {
                        if (nestedProp.getName().equals("pattern")) {
                            patternChild = nestedProp;
                            System.out.println(patternChild);
                        }
                        if (nestedProp.getName().equals("type")) {
                            typeChild = nestedProp;
                            System.out.println(typeChild);
                        }
                    }
                }
            }
        }
    }

    private void processPatternProps(List<PropertyDescriptor> patternProps, List<PropertyDescriptor> leftoverProps, PropertyDescriptor propertyDescriptor) {
        if(!patternProps.isEmpty()) {
            if (typeChild.getValue() != null && patternChild.getValue() != null) {
                if (typeChild.getValue().equals("INCLUDE")) {
                    PropertyDescriptor newChild = new PropertyDescriptor("includePattern",
                            propertyDescriptor, patternChild.getValue());

                    leftoverProps.add(0, newChild);
                } else if (typeChild.getValue().equals("EXCLUDE")) {
                    PropertyDescriptor newChild = new PropertyDescriptor("excludePattern",
                            propertyDescriptor,
                            patternChild.getValue());

                    leftoverProps.add(0, newChild);
                }
            }
        }
        propertyDescriptor.replaceProperties(leftoverProps);
        initChildren(propertyDescriptor);
    }

}

