package com.cloud.opentelemetry.javaagent;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * See <a
 * href="https://github.com/open-telemetry/opentelemetry-specification/blob/master/specification/trace/sdk.md#span-exporter">
 * OpenTelemetry Specification</a> for more information about {@link SpanExporter}.
 *
 * @see DemoSdkTracerProviderConfigurer
 */
public class DemoSpanExporter implements SpanExporter {
  final AtomicBoolean isExportRequired  = new AtomicBoolean(false);
  @Override
  public CompletableResultCode export(Collection<SpanData> spans) {
    // 这里可以用于导出Span到自定义数据处理后端
//    System.out.printf("%d spans exported%n", spans.size());
    spans.forEach(spanData -> {
      Attributes attributes = spanData.getAttributes();
      String url = attributes.get(AttributeKey.stringKey("http.url"));
      if (StringUtils.isBlank(url)) {
        return;
      }
      if (url.contains("/nacos/v1/cs/configs")) {
        isExportRequired.set(true);
      }
    });
    if (isExportRequired.getAndSet(false)) {
      spans.forEach(spanData -> {
        System.out.println(spanData.getAttributes().get(AttributeKey.stringKey("http.url")));

      });
    }
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode flush() {
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    return CompletableResultCode.ofSuccess();
  }
}
