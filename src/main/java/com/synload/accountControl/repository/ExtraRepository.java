package com.synload.accountControl.repository;

import com.synload.accountControl.domain.ExtraData;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ExtraRepository extends GraphRepository<ExtraData> {

}