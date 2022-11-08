package com.tomgs.learning;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.tomgs.learning.constant.DataBaseType;
import com.tomgs.learning.constant.DataStructType;
import com.tomgs.learning.constant.FieldDataType;
import com.tomgs.learning.model.DynamicObject;
import com.tomgs.learning.model.FieldObject;
import com.tomgs.learning.model.MethodObject;
import com.tomgs.learning.model.ParameterObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * DynamicObjectTest
 *
 * @author tomgs
 * @since 1.0
 */
public class DynamicObjectTest {

    @Test
    public void buildDynamicObject() {
        FieldObject fieldObject = FieldObject.builder()
                .id(20L)
                .name("id")
                .jsonName("id")
                .fieldDataStructType(DataStructType.NONE)
                .fieldDataType(FieldDataType.LONG)
                .build();
        FieldObject fieldObject2 = FieldObject.builder()
                .id(21L)
                .name("name")
                .jsonName("name")
                .fieldDataStructType(DataStructType.NONE)
                .fieldDataType(FieldDataType.STRING)
                .build();

        ParameterObject parameterObject = ParameterObject.builder()
                .id(40L)
                .parameterStructType(DataStructType.NONE)
                .fieldObject(fieldObject)
                .build();

        ParameterObject parameterObject2 = ParameterObject.builder()
                .id(41L)
                .parameterStructType(DataStructType.LIST)
                .fieldObject(fieldObject)
                .build();

        MethodObject methodObject = MethodObject.builder()
                .id(30L)
                .name("getName")
                .params(CollectionUtil.newArrayList(parameterObject))
                .resultStructType(DataStructType.NONE)
                .resultDynObjId(11L)
                .build();
        MethodObject methodObject2 = MethodObject.builder()
                .id(31L)
                .name("getNames")
                .params(CollectionUtil.newArrayList(parameterObject2))
                .resultStructType(DataStructType.LIST)
                .resultDynObjId(11L)
                .build();

        DynamicObject dynamicObject = DynamicObject.builder()
                .id(10L)
                .name("test_demo")
                .dataBaseType(DataBaseType.MYSQL)
                .primaryKey("id")
                .fields(CollectionUtil.newArrayList(fieldObject, fieldObject2))
                .methods(CollectionUtil.newArrayList(methodObject, methodObject2))
                .build();

        System.out.println(JSONUtil.toJsonStr(dynamicObject));

        String metadata = "{\"methods\":[{\"resultStructType\":\"NONE\",\"params\":[{\"fieldObject\":{\"fieldDataStructType\":\"NONE\"," +
                "\"jsonName\":\"id\",\"name\":\"id\",\"id\":20,\"fieldDataType\":\"LONG\"},\"parameterStructType\":\"NONE\",\"id\":40}]," +
                "\"resultDynObjId\":11,\"name\":\"getName\",\"id\":30},{\"resultStructType\":\"LIST\",\"params\":" +
                "[{\"fieldObject\":{\"fieldDataStructType\":\"NONE\",\"jsonName\":\"id\",\"name\":\"id\",\"id\":20,\"fieldDataType\":" +
                "\"LONG\"},\"parameterStructType\":\"LIST\",\"id\":41}],\"resultDynObjId\":11,\"name\":\"getNames\",\"id\":31}]," +
                "\"dataBaseType\":\"MYSQL\",\"name\":\"test_demo\",\"id\":10,\"fields\":[{\"fieldDataStructType\":\"NONE\",\"jsonName\":\"id\"," +
                "\"name\":\"id\",\"id\":20,\"fieldDataType\":\"LONG\"},{\"fieldDataStructType\":\"NONE\",\"jsonName\":\"name\",\"name\":" +
                "\"name\",\"id\":21,\"fieldDataType\":\"STRING\"}],\"primaryKey\":\"id\"}";

        final DynamicObject dynamicObject1 = JSONUtil.toBean(metadata, DynamicObject.class);
        Assert.assertEquals(dynamicObject1, dynamicObject);
    }

    @Test
    public void reflectionDynamicObject() {
        DynamicObject dynamicObject = DynamicObject.builder().build();
        final Class<? extends DynamicObject> aClass = dynamicObject.getClass();
    }

}
