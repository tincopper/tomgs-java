# 如何编译生成protoc插件代码和生成Dump文件代码
在父母录`learning-proto`项目执行，`mvn clean install`即可。

如果直接在`proto-base`项目执行`protobuf:compile`只会生成基础的java代码，不会生成插件代码。