# 命令行参数

```shell
-javaagent:.agent/opentelemetry-javaagent.jar -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-resource-server
-javaagent:.agent/opentelemetry-javaagent.jar -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-gateway-server
-javaagent:.agent/opentelemetry-javaagent.jar -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-user-server


-javaagent:/Users/agoodman/workspace/public/opentelemetry-java-instrumentation/examples/distro/agent/build/libs/agent-1.0-SNAPSHOT.jar  -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-resource-server
-javaagent:/Users/agoodman/workspace/public/opentelemetry-java-instrumentation/examples/distro/agent/build/libs/agent-1.0-SNAPSHOT.jar  -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-gateway-server
-javaagent:/Users/agoodman/workspace/public/opentelemetry-java-instrumentation/examples/distro/agent/build/libs/agent-1.0-SNAPSHOT.jar  -Dotel.javaagent.debug=true -Dotel.resource.attributes=service.name=cloud-user-server
```