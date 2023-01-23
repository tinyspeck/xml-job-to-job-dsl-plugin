package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

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
			includePattern("results/*")
			deleteDirectories(false)
			cleanupParameter("test")
			deleteCommand("test")
		}
 */

public class DSLCleanupPatternStrategy extends DSLObjectStrategy {

    private final String name;

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLCleanupPatternStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;

        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        PropertyDescriptor parent = propertyDescriptor.getParent();

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

        if (typeChild.getValue().equals("INCLUDE")) {
            PropertyDescriptor newChild = new PropertyDescriptor("includePattern",
                    parent.getParent(),
                    patternChild.getValue());

            parent.addProperty(newChild);

        } else if (typeChild.getValue().equals("EXCLUDE")) {
            PropertyDescriptor newChild = new PropertyDescriptor("excludePattern",
                    parent.getParent(),
                    patternChild.getValue());

            parent.addProperty(newChild);
        }
    }

        public String toDSL() {
        String childrenDSL = getChildrenDSL();
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
