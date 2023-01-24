package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.IValueStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;

public class DSLHudsonNotificationPropertyStrategy extends DSLObjectStrategy {
    private final String name;
    private final String LATEST_UPDATED_PLUGIN_VERSION = "1.15";

    public DSLHudsonNotificationPropertyStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
        boolean doCastChildren = determineOutdatedOrNoPluginVersion(propertyDescriptor);
        if(doCastChildren){
            castChildrenToLatestFormat(propertyDescriptor);
        }
    }

    public DSLHudsonNotificationPropertyStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
        boolean doCastChildren = determineOutdatedOrNoPluginVersion(propertyDescriptor);
        if(doCastChildren){
            castChildrenToLatestFormat(propertyDescriptor);
        }
    }

    private boolean determineOutdatedOrNoPluginVersion(PropertyDescriptor propertyDescriptor){
        if(propertyDescriptor.getAttributes() == null){
            return true;
        } else {
            // Assuming the plugin version looks like this: notification@1.15
            Float pluginVersion = Float.parseFloat(propertyDescriptor.getAttributes().get("plugin").split("@")[1]);
            // Float.compare 0 means f1 is equal to f2 // negative value means f1 is less than f2 // positive value f1 is greater than f2
            int comparePluginToLatestVersion = Float.compare(pluginVersion, Float.parseFloat(this.LATEST_UPDATED_PLUGIN_VERSION));
            if(comparePluginToLatestVersion >= 0){
                return false;
            } else {
                return true;
            }
        }
    }

    private void castChildrenToLatestFormat(PropertyDescriptor propertyDescriptor){
        // Just grab all the bottom level grandchildren at once
        List<PropertyDescriptor> children = findViableChildren(propertyDescriptor);
        for (PropertyDescriptor child : children) {
            if(child.getAttributes() != null && child.getAttributes().get("ChangedName") == "true"){
                break;
            } else if(child.getName().equals("url")){
                child.addAttribute("ChangedName", "true");
                child.changeName("urlInfo");
                List<PropertyDescriptor> grandChildren = new ArrayList<>();
                PropertyDescriptor urlType = new PropertyDescriptor(
                        "urlType",
                        null,
                        "PUBLIC"
                );

                PropertyDescriptor urlOrId = new PropertyDescriptor(
                        "urlOrId",
                        null,
                        child.getValue()
                );
                grandChildren.add(urlType);
                grandChildren.add(urlOrId);

                child.replaceProperties(grandChildren);
                initChildren(child.getParent());
            }
        }

    }

    private ArrayList<PropertyDescriptor> findViableChildren(PropertyDescriptor propertyDescriptor){
        ArrayList<PropertyDescriptor> viableChildren = new ArrayList<>();
        List<PropertyDescriptor> children = propertyDescriptor.getProperties();

        for(PropertyDescriptor child : children){
            if(child.getProperties() == null || child.getProperties().size() == 0){
                viableChildren.add(child);
            } else {
                viableChildren.addAll(findViableChildren(child));
            }
        }

        return viableChildren;
    }
}
