package com.example.JounralApplication.Services;

import com.example.JounralApplication.Repository.JounralEntryRepository;
import com.example.JounralApplication.entity.JounralEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class JounralEntryServices {

    @Autowired
    private JounralEntryRepository jounralEntryRepository;

    public JounralEntry saveEntry(JounralEntry jounralEntry) {
        if (jounralEntry.getCreatedAt() == null) {
            jounralEntry.setCreatedAt(new Date());
        }
        jounralEntry.setUpdatedAt(new Date());
        return jounralEntryRepository.save(jounralEntry);
    }

    public List<JounralEntry> getAllEntries() {
        return jounralEntryRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<JounralEntry> getEntryById(String id) {
        return jounralEntryRepository.findById(id);
    }

    public List<JounralEntry> searchByTitle(String title) {
        return jounralEntryRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<JounralEntry> searchByContent(String content) {
        return jounralEntryRepository.findByContentContainingIgnoreCase(content);
    }

    public List<JounralEntry> getEntriesByDateRange(Date startDate, Date endDate) {
        return jounralEntryRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public void deleteEntry(String id) {
        jounralEntryRepository.deleteById(id);
    }

    public JounralEntry updateEntry(String id, JounralEntry updatedEntry) {
        Optional<JounralEntry> existingEntryOpt = jounralEntryRepository.findById(id);
        
        if (existingEntryOpt.isPresent()) {
            JounralEntry existingEntry = existingEntryOpt.get();
            
            // Update only non-null fields
            if (updatedEntry.getTitle() != null) {
                existingEntry.setTitle(updatedEntry.getTitle());
            }
            if (updatedEntry.getContent() != null) {
                existingEntry.setContent(updatedEntry.getContent());
            }
            
            // Always update the updatedAt timestamp
            existingEntry.setUpdatedAt(new Date());
            
            return jounralEntryRepository.save(existingEntry);
        }
        
        return null;
    }
}
