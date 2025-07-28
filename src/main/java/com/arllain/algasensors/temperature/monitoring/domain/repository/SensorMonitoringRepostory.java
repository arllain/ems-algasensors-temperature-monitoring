package com.arllain.algasensors.temperature.monitoring.domain.repository;

import com.arllain.algasensors.temperature.monitoring.domain.model.SensorId;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepostory extends JpaRepository<SensorMonitoring, SensorId> {
}
