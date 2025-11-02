package com.arllain.algasensors.temperature.monitoring.domain.service;

import com.arllain.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorId;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.arllain.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.arllain.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.arllain.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepostory;
import com.arllain.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final SensorMonitoringRepostory sensorMonitoringRepostory;
    private final TemperatureLogRepository temperatureLogRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        log.info("processTemperatureReading");
        if(temperatureLogData.getValue().equals(10.5)) {
            throw new RuntimeException("Test error");
        }
        sensorMonitoringRepostory.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData));
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if(sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepostory.save(sensor);
            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .value(temperatureLogData.getValue())
                    .build();
            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature Updated: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
        }else {
            logIgnoredTemperature(temperatureLogData);
        }
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Temperature Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
