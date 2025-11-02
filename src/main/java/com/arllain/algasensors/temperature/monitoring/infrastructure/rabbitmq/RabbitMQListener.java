package com.arllain.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.arllain.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.arllain.algasensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.arllain.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQCofing.QUEUE_ALERTING;
import static com.arllain.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQCofing.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    @SneakyThrows
    public void handleProcessTemperature(@Payload  TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        Thread.sleep(Duration.ofSeconds(5));
    }

    @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
    @SneakyThrows
    public void handleAlerting(@Payload  TemperatureLogData temperatureLogData) {
        temperatureMonitoringService.processTemperatureReading(temperatureLogData);
        log.info("Alerting: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        Thread.sleep(Duration.ofSeconds(5));
    }

}
