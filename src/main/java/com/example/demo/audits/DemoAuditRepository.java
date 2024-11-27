package com.example.demo.audits;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DemoAuditRepository extends CosmosRepository<DemoAudit, String> {

    List<DemoAudit> findAll();

    List<DemoAudit> findAllByPrincipalAndTimestampGreaterThanEqualOrderByTimestampAsc(String principal, Instant instant);

	List<DemoAudit> findAllByPrincipalAndTypeInAndTimestampGreaterThanEqualOrderByTimestampAsc(String principal, List<String> auditTypes, Instant instant);

    boolean existsByPrincipalAndType(String principal, String auditType);

    List<DemoAudit> findAllByPrincipalAndSubtypeIsNull(String principal);

    @Query("SELECT * FROM audits a where ARRAY_CONTAINS(a.eligibleEvents, @eligibleEventCode)")
    List<DemoAudit> findByPrincipalAndEligibleEventsContains(String principal, String eligibleEventCode);

    void deleteAllByPrincipalIn(List<String> principals);

}
