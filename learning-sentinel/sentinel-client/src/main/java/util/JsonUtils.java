package util;

import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;

/**
 *  
 *
 * @author tomgs
 * @version 2020/2/14 1.0 
 */
public class JsonUtils {

    public static void main(String[] args) {
        String source = "[{\"app\":\"jd-dev\",\"gmtCreate\":1581660407768,\"gmtModified\":1581665914122,\"id\":677878411900747776,\"ip\":\"192.168.56.1\",\"port\":8720,\"rule\":{\"burstCount\":0,\"clusterConfig\":{\"fallbackToLocalWhenFail\":true,\"sampleCount\":10,\"thresholdType\":0,\"windowIntervalMs\":1000},\"clusterMode\":false,\"controlBehavior\":0,\"count\":1.0,\"durationInSec\":3,\"grade\":1,\"limitApp\":\"default\",\"maxQueueingTimeMs\":0,\"paramFlowItemList\":[],\"paramIdx\":0,\"resource\":\"TestResource\"}}]";
        List<ParamFlowRule> paramFlowRules = JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
        });
        for (ParamFlowRule paramFlowRule : paramFlowRules) {
            //paramFlowRule.get
        }
        System.out.println(paramFlowRules);
    }
    
}
