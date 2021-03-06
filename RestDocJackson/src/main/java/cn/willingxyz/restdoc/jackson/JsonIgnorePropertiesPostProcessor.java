package cn.willingxyz.restdoc.jackson;

import cn.willingxyz.restdoc.core.models.PropertyItem;
import cn.willingxyz.restdoc.core.models.PropertyModel;
import cn.willingxyz.restdoc.core.models.TypeContext;
import cn.willingxyz.restdoc.core.parse.postprocessor.IPropertyPostProcessor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * com.fasterxml.jackson.annotation.JsonIgnoreProperties
 */
@AutoService(IPropertyPostProcessor.class)
public class JsonIgnorePropertiesPostProcessor implements IPropertyPostProcessor {
    @Override
    public PropertyModel postProcess(PropertyModel propertyModel, TypeContext typeContext) {
        // 1. 当@JsonIgnoreProperties出现在字段或getter/setter方法上时
        JsonIgnoreProperties jsonIgnoreProperties = propertyModel.getPropertyItem().getAnnotation(JsonIgnoreProperties.class);
        if (jsonIgnoreProperties != null && arrayContains(jsonIgnoreProperties.value(), propertyModel.getName())) {
            return null;
        }
        // 2. 当@JsonIgnoreProperties出现在类上
        Class clazz = propertyModel.getPropertyItem().getDeclaringClass();
        if (clazz != null) {
            List<JsonIgnoreProperties> jsonIgnorePropertiesList = getAnnotation(clazz);
            if (jsonIgnorePropertiesList.stream().anyMatch(o -> arrayContains(o.value(), propertyModel.getName()))) {
                return null;
            }
        }
        // 参数所属的类作为类的属性
        PropertyItem parentPropertyItem = propertyModel.getParentPropertyItem();
        if (parentPropertyItem != null && parentPropertyItem.getPropertyType() instanceof Class) {
            clazz = (Class) parentPropertyItem.getPropertyType();
            List<JsonIgnoreProperties> jsonIgnorePropertiesList = getAnnotation(clazz);
            if (jsonIgnorePropertiesList.stream().anyMatch(o -> arrayContains(o.value(), propertyModel.getName()))) {
                return null;
            }
        }
        // 属性所属的类作为作为方法参数
        if (parentPropertyItem == null && typeContext.getParameter() != null
                && clazz != null && clazz.isAssignableFrom(typeContext.getParameter().getType())) {
            List<JsonIgnoreProperties> jsonIgnorePropertiesList = getAnnotation(typeContext.getParameter().getType());
            if (jsonIgnorePropertiesList.stream().anyMatch(o -> arrayContains(o.value(), propertyModel.getName()))) {
                return null;
            }
        }

        return propertyModel;
    }

    // 获取所有的注解，包括父类的
    private List<JsonIgnoreProperties> getAnnotation(Class clazz) {
        List<JsonIgnoreProperties> annotations = new ArrayList<>();
        while (clazz != Object.class && clazz != null) {
            JsonIgnoreProperties annotation = (JsonIgnoreProperties) clazz.getDeclaredAnnotation(JsonIgnoreProperties.class);
            if (annotation != null) {
                annotations.add(annotation);
            }
            clazz = clazz.getSuperclass();
        }
        return annotations;
    }

    private boolean arrayContains(String[] arr, String value) {
        return Arrays.asList(arr).contains(value);
    }
}
