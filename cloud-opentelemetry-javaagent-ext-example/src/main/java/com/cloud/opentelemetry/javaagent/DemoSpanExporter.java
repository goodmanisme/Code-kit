package com.cloud.opentelemetry.javaagent;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.util.Collection;

/**
 * See <a
 * href="https://github.com/open-telemetry/opentelemetry-specification/blob/master/specification/trace/sdk.md#span-exporter">
 * OpenTelemetry Specification</a> for more information about {@link SpanExporter}.
 *
 * @see DemoSdkTracerProviderConfigurer
 */
public class DemoSpanExporter implements SpanExporter {
  @Override
  public CompletableResultCode export(Collection<SpanData> spans) {
    // 这里可以用于导出Span到自定义数据处理后端
    System.out.printf("%d spans exported%n", spans.size());
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
