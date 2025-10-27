package com.arllain.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.arllain.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static com.arllain.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQCofing.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @RabbitListener(queues = QUEUE)
    @SneakyThrows
    public void handle(@Payload  TemperatureLogData temperatureLogData, @Headers Map<String, Object> headers) {
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info("Temperature updated: Sensor {} Temp {} ", sensorId, temperature);
        log.info("Headers: {}", headers);

        Thread.sleep(Duration.ofSeconds(5));
    }
}
