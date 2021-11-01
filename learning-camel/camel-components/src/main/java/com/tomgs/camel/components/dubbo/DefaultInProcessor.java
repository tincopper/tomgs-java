package com.tomgs.camel.components.dubbo;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.common.HttpMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 统一的参数处理类，我们不需要关心是什么格式的数据。在我们这里body必须是JSONObject类型的
 */
@Slf4j
public class DefaultInProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpMessage message = (HttpMessage) exchange.getMessage();
        String body = message.getBody(String.class);
        HttpServletRequest request = message.getRequest();
        JSONObject parameters = new JSONObject();
        //处理请求参数
        log.info("处理请求参数信息！");
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            parameters.put(name, request.getParameter(name));
        }
        log.info("请求参数内容为：{}", parameters.toJSONString());
        log.info("处理请求提信息：{}", body);
        try {
            JSONObject obj = JSONObject.parseObject(body);
            parameters.putAll(obj);
        } catch (Exception e) {
            log.error("请求体格式不正确。{}", e.getMessage());
            throw e;
        }
        message.setBody(parameters.toJSONString());
    }

}
