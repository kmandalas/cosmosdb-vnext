package com.example.demo.audits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(AuditEvent event) {
		try {
			var auditAppEvent = new AuditApplicationEvent(event);
			applicationEventPublisher.publishEvent(auditAppEvent);
		} catch (Exception e) {
			log.error("Cosmos/General Error while auditing event {}", event, e);
		}
	}

}
