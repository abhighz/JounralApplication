package com.example.JounralApplication.Repository;

import com.example.JounralApplication.entity.JounralEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JounralEntryRepository extends MongoRepository<JounralEntry, String> {
    List<JounralEntry> findByTitleContainingIgnoreCase(String title);
    List<JounralEntry> findByContentContainingIgnoreCase(String content);
    List<JounralEntry> findByCreatedAtBetween(Date startDate, Date endDate);
    List<JounralEntry> findAllByOrderByCreatedAtDesc();
}
