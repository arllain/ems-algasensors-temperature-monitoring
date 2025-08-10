package com.arllain.algasensors.temperature.monitoring.api.controller;

import com.arllain.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorId;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.arllain.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepostory;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

    private final SensorMonitoringRepostory sensorMonitoringRepostory;

    @GetMapping
    public SensorMonitoringOutput getDetail(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);

        return  SensorMonitoringOutput.builder()
                .id(sensorMonitoring.getId().getValue())
                .enabled(sensorMonitoring.getEnabled())
                .lastTemperature(sensorMonitoring.getLastTemperature())
                .updateAt(sensorMonitoring.getUpdatedAt())
                .build();
    }

    private SensorMonitoring findByIdOrDefault(TSID sensorId) {
        return sensorMonitoringRepostory.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updatedAt(null)
                        .build());
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);
        if (sensorMonitoring.getEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        sensorMonitoring.setEnabled(true);
        sensorMonitoringRepostory.saveAndFlush(sensorMonitoring);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable TSID sensorId) {
        SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);
        sensorMonitoring.setEnabled(false);
        sensorMonitoringRepostory.saveAndFlush(sensorMonitoring);
    }

}
