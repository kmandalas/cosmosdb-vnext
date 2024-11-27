package com.example.demo.audits;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomAuditEventRepository implements AuditEventRepository {

    private final DemoAuditRepository demoAuditRepository;

    @Override
    public void add(AuditEvent event) {
        var demoAudit = DemoAudit.builder()
                .principal(event.getPrincipal())
                .type(event.getType())
                .timestamp(event.getTimestamp())
                .data(event.getData())
                .build();

        demoAuditRepository.save(demoAudit);
    }

    @Override
    public List<AuditEvent> find(String principal, Instant after, String type) {
        var demoAudits = demoAuditRepository.findAll();
        return demoAudits.stream()
                .map(this::convertToAuditEvent)
                .toList();
    }

    private AuditEvent convertToAuditEvent(DemoAudit demoAudit) {
        return new AuditEvent(
                demoAudit.getTimestamp(),
                demoAudit.getPrincipal(),
                demoAudit.getType(),
                demoAudit.getData());
    }

}
