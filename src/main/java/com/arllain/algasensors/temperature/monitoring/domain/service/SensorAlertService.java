package com.arllain.algasensors.temperature.monitoring.domain.service;

import com.arllain.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorId;
import com.arllain.algasensors.temperature.monitoring.domain.repository.SensorAlertRepostory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepostory sensorAlertRepository;

    @Transactional
    public void handleAlert(TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {
                    if(alert.getMaxTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                        log.info("Alert Max Temp: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    }else if (alert.getMinTemperature() != null && temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                        log.info("Alert Min Temp: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
                    }else {
                       logIgnoredAert(temperatureLogData);
                    }
                }, () -> {
                    logIgnoredAert(temperatureLogData);
                });

    }

    private static void logIgnoredAert(TemperatureLogData temperatureLogData) {
        log.info("Alert Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
