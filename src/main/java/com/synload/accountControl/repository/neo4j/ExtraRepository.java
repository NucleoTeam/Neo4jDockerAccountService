package com.synload.accountControl.repository.neo4j;

import com.synload.accountControl.domain.neo4j.ExtraData;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ExtraRepository extends GraphRepository<ExtraData> {

}
