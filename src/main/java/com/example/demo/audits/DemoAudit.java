package com.example.demo.audits;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

@Builder
@Data
@Container(containerName = "audits")
public class DemoAudit {

    @Id
    @GeneratedValue
    private String id;

    private final Instant timestamp;

    @PartitionKey
    private final String principal;

    private final String type;

    private final Map<String, Object> data;

}
