/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.sentinel.spi;

/**
 * @author hantianwei@gmail.com
 * @since 1.5.0
 */
public final class ApolloConfigUtil {

  private static final String FLOW_DATA_ID_POSTFIX = "flow-rules";
  private static final String DEGRADE_DATA_ID_POSTFIX = "degrade-rules";
  private static final String AUTHORITY_DATA_ID_POSTFIX = "authority-rules";
  private static final String PARAM_FLOW_DATA_ID_POSTFIX = "param-flow-rules";
  private static final String SYSTEM_DATA_ID_POSTFIX = "system-rules";

  private ApolloConfigUtil() {
  }

  public static String getAppName() {
    return System.getProperty("project.name");
  }

  public static String getNamespaceName() {
    // TODO: 根据自己具体配置逻辑处理
    return System.getProperty("apollo.namespace", "application");
  }

  public static String getFlowDataId() {
    return getFlowDataId(getAppName());
  }

  public static String getDegradeDataId() {
    return getDegradeDataId(getAppName());
  }

  public static String getAuthorityDataId() {
    return getAuthorityDataId(getAppName());
  }

  public static String getParamFlowDataId() {
    return getParamFlowDataId(getAppName());
  }

  public static String getSystemDataId() {
    return getSystemDataId(getAppName());
  }

  public static String getFlowDataId(String appName) {
    return String.format("%s_%s", appName, FLOW_DATA_ID_POSTFIX);
  }

  public static String getDegradeDataId(String appName) {
    return String.format("%s_%s", appName, DEGRADE_DATA_ID_POSTFIX);
  }

  public static String getAuthorityDataId(String appName) {
    return String.format("%s_%s", appName, AUTHORITY_DATA_ID_POSTFIX);
  }

  public static String getParamFlowDataId(String appName) {
    return String.format("%s_%s", appName, PARAM_FLOW_DATA_ID_POSTFIX);
  }

  public static String getSystemDataId(String appName) {
    return String.format("%s_%s", appName, SYSTEM_DATA_ID_POSTFIX);
  }

}
