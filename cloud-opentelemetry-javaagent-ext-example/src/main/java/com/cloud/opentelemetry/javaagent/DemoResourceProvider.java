package com.cloud.opentelemetry.javaagent;

import com.google.auto.service.AutoService;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

@AutoService(ResourceProvider.class)
public class DemoResourceProvider implements ResourceProvider {
  @Override
  public Resource createResource(ConfigProperties config) {
    String applicationName = config.getString(ResourceAttributes.SERVICE_NAME.getKey());
    Attributes attributes = Attributes.builder()
            .put("custom.resource", "demo")
            .put(ResourceAttributes.SERVICE_NAME.getKey(),applicationName)
            .build();
    return Resource.create(attributes);
  }
}
