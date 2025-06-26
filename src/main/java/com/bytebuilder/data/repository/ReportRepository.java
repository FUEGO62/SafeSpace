package com.bytebuilder.data.repository;

import com.bytebuilder.data.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {

}
