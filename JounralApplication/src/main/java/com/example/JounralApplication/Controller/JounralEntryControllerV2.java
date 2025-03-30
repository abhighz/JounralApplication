package com.example.JounralApplication.Controller;

import com.example.JounralApplication.Services.JounralEntryServices;
import com.example.JounralApplication.entity.JounralEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abc")
public class JounralEntryControllerV2 {

    @Autowired
    private JounralEntryServices jounralEntryServices;

    @GetMapping
    public List<JounralEntry> getall() {
        return jounralEntryServices.getAllEntries();
    }

    @PostMapping
    public ResponseEntity<JounralEntry> createEntry(@RequestBody JounralEntry myEntry) {
        JounralEntry savedEntry = jounralEntryServices.saveEntry(myEntry);
        return ResponseEntity.ok(savedEntry);
    }

    @GetMapping("id/{myid}")
    public ResponseEntity<JounralEntry> GetjouralEntryByid(@PathVariable String myid) {
        return jounralEntryServices.getEntryById(myid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<Void> DeletejouralEntryByid(@PathVariable String myid) {
        jounralEntryServices.deleteEntry(myid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("id/{id}")
    public ResponseEntity<JounralEntry> UpdatejouralEntryByid(@PathVariable String id, @RequestBody JounralEntry myEntry) {
        JounralEntry updatedEntry = jounralEntryServices.updateEntry(id, myEntry);
        if (updatedEntry != null) {
            return ResponseEntity.ok(updatedEntry);
        }
        return ResponseEntity.notFound().build();
    }
}
