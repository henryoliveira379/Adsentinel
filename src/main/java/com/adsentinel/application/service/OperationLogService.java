package com.adsentinel.application.service;

import com.adsentinel.domain.model.OperationLog;
import com.adsentinel.infrastructure.persistence.OperationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationLogService {

    private final OperationLogRepository logRepository;

    public OperationLogService(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public void log(String operator, String action, String description, Long entityId, String entityType) {
        OperationLog log = new OperationLog();
        log.setTimestamp(LocalDateTime.now());
        log.setOperatorName(operator);
        log.setActionType(action);
        log.setDescription(description);
        log.setEntityId(entityId);
        log.setEntityType(entityType);
        logRepository.save(log);
    }

    public List<OperationLog> getLogsForEntity(Long entityId, String entityType) {
        return logRepository.findByEntityIdAndEntityType(entityId, entityType);
    }
    
    public List<OperationLog> getAllLogs() {
        return logRepository.findAll();
    }
}
