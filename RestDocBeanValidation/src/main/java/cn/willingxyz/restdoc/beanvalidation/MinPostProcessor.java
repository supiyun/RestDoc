package cn.willingxyz.restdoc.beanvalidation;

import cn.willingxyz.restdoc.core.models.PropertyItem;
import cn.willingxyz.restdoc.core.models.PropertyModel;
import cn.willingxyz.restdoc.core.parse.IPropertyPostProcessor;
import cn.willingxyz.restdoc.core.parse.utils.TextUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * javax.validation.constraints.Min
 */
public class MinPostProcessor extends AbstractPropertyPostProcessor {
    @Override
    public void postProcessInternal(PropertyModel propertyModel) {
        Min minAnno = propertyModel.getPropertyItem().getAnnotation(Min.class);
        if (minAnno != null)
        {
            propertyModel.setDescription(
                    TextUtils.combine(propertyModel.getDescription(),
                            String.format(" (值大于等于%s)", minAnno.value())
                    )
            );
        }
    }
}
