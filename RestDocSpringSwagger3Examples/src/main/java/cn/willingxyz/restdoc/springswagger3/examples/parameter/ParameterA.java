package cn.willingxyz.restdoc.springswagger3.examples.parameter;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 类型ParameterA类
 */
@Data
public class ParameterA
{
    /**
     * 参数id
     */
    private int _id;
    /**
     * 参数name
     */
    private String _name;
    /**
     * 参数parameterB
     */
    private ParameterB _parameterB;
    /**
     * 参数ParameterBArray数组
     */
    private ParameterB[] _parameterBArray;
}
