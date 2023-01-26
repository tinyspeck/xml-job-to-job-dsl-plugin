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

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        List<PropertyDescriptor> leftoverProps = new ArrayList<>();
        List<PropertyDescriptor> patternProps = new ArrayList<>();

        // separating the nested properties to handle later
        for (PropertyDescriptor child : children) {
            if (!child.getName().equals("patterns")) {
                leftoverProps.add(child);
            }
            if (child.getName().equals("patterns")) {
                patternProps.add(child);
            }
        }

        // extracting type and pattern property descriptors
        PropertyDescriptor typeChild = null;
        PropertyDescriptor patternChild = null;
        for (PropertyDescriptor prop : patternProps) {
            if (prop.getName().equals("patterns")) {
                for (PropertyDescriptor innerProp : prop.getProperties()) {
                    for (PropertyDescriptor nestedProp : innerProp.getProperties()) {
                        if (nestedProp.getName().equals("pattern")) {
                            patternChild = nestedProp;
                        }
                        if (nestedProp.getName().equals("type")) {
                            typeChild = nestedProp;
                        }
                    }
                }
            }
        }

        // create the new property descriptor and add it to the rest of the children under preBuildCleanup
        if(!patternProps.isEmpty()) {
            if (!typeChild.getValue().equals(null) && !patternChild.getValue().equals(null)) {
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

                propertyDescriptor.replaceProperties(leftoverProps);
                System.out.println(propertyDescriptor.getProperties());
                initChildren(propertyDescriptor);
            }
        }

    }
}

