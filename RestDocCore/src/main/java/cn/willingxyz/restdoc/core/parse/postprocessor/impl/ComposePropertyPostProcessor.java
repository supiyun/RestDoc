package cn.willingxyz.restdoc.core.parse.postprocessor.impl;

import cn.willingxyz.restdoc.core.models.PropertyModel;
import cn.willingxyz.restdoc.core.models.TypeContext;
import cn.willingxyz.restdoc.core.parse.postprocessor.IPropertyPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 依次执行多个IPropertyPostProcessor
 */
public class ComposePropertyPostProcessor implements IPropertyPostProcessor {

    private ArrayList<IPropertyPostProcessor> _processors;

    public ComposePropertyPostProcessor(IPropertyPostProcessor... processors)
    {
        _processors = new ArrayList<>(Arrays.asList(processors));
    }

    public ComposePropertyPostProcessor()
    {
        _processors = new ArrayList<>();
    }

    public void add(IPropertyPostProcessor propertyPostProcessor)
    {
        _processors.add(propertyPostProcessor);
    }

    @Override
    public PropertyModel postProcess(PropertyModel propertyModel, TypeContext typeContext) {
        for (IPropertyPostProcessor postProcessor : _processors)
        {
            propertyModel = postProcessor.postProcess(propertyModel, typeContext);
            if (propertyModel == null)
                return null;
        }
        return propertyModel;
    }
}
