package com.synload.accountControl.repository.neo4j;

import com.synload.accountControl.domain.PermissionData;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PermissionRepository extends GraphRepository<PermissionData> {
    PermissionData getByName(String name);
}
