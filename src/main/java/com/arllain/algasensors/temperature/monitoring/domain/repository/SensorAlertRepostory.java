package com.arllain.algasensors.temperature.monitoring.domain.repository;

import com.arllain.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.arllain.algasensors.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepostory extends JpaRepository<SensorAlert, SensorId> {
}
