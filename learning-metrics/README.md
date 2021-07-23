# metrics监控类库

> 官方文档https://metrics.dropwizard.io/3.1.0/manual/core/

maven依赖：
https://mvnrepository.com/artifact/io.dropwizard.metrics
```xml
<dependency>
     <groupId>io.dropwizard.metrics</groupId> 
    <artifactId>metrics-core</artifactId> 
    <version>4.2.3</version> 
</dependency>
```
# metrics提供的基本功能
Metrics提供了五个基本的度量类型：
1. Gauges（度量）
2. Counters（计数器）
3. Histograms（直方图数据）
4. Meters（TPS计算器）
5. Timers（计时器）

Metrics中MetricRegistry是中心容器，它是程序中所有度量的容器，所有新的度量工具都要注册到一个MetricRegistry实例中才可以使用，尽量在一个应用中保持让这个MetricRegistry实例保持单例。

