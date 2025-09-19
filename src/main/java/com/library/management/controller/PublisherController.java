package com.library.management.controller;

import com.library.management.dto.PublisherDTO;
import com.library.management.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublisherDTO>> searchPublishers(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.searchPublishers(name));
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.createPublisher(publisherDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id,
                                                        @RequestBody PublisherDTO publisherDTO) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisherDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.ok(Map.of("message", "Publisher deleted successfully"));

    }
}