package com.example.demo.audits;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DemoAuditRepository extends CosmosRepository<DemoAudit, String> {

    List<DemoAudit> findAll();

    List<DemoAudit> findAllByPrincipalAndTimestampGreaterThanEqualOrderByTimestampAsc(String principal, Instant instant);

	List<DemoAudit> findAllByPrincipalAndTypeInAndTimestampGreaterThanEqualOrderByTimestampAsc(String principal, List<String> auditTypes, Instant instant);

    void deleteAllByPrincipalIn(List<String> principals);

}
