package com.example.demo.audits;

import com.example.demo.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DemoAuditRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private DemoAuditRepository demoAuditRepository;

    @Autowired
    private AuditService auditService;

    @Test
    void testFindAllByPrincipalAndTimestampGreaterThanEqualOrderByTimestampAsc() {

        Map<String, Object> auditData = new HashMap<>();
        auditData.put("request", "sample request");
        auditData.put("response", "sample response");
        auditService.publishEvent(new AuditEvent("userA", "USER_ACTION_X", auditData));

       var audits = demoAuditRepository.findAllByPrincipalAndTimestampGreaterThanEqualOrderByTimestampAsc("userA",
               Instant.now().minus(Duration.ofMinutes(1)));
       assertThat(audits).hasSize(1);

        var auditsByTypes = demoAuditRepository.findAllByPrincipalAndTypeInAndTimestampGreaterThanEqualOrderByTimestampAsc("userA",
                List.of("USER_ACTION_X", "USER_ACTION_Y"), Instant.now().minus(Duration.ofMinutes(1)));
        assertThat(auditsByTypes).hasSize(1);
    }

}
