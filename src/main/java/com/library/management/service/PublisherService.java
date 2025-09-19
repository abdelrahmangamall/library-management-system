package com.library.management.service;

import com.library.management.dto.PublisherDTO;
import com.library.management.entity.Publisher;
import com.library.management.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PublisherDTO getPublisherById(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + id));
        return convertToDTO(publisher);
    }

    public List<PublisherDTO> searchPublishers(String name) {
        return publisherRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        if (publisherRepository.existsByNameIgnoreCase(publisherDTO.getName())) {
            throw new RuntimeException("Publisher name already exists: " + publisherDTO.getName());
        }

        Publisher publisher = convertToEntity(publisherDTO);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return convertToDTO(savedPublisher);
    }

    @Transactional
    public PublisherDTO updatePublisher(Long id, PublisherDTO publisherDTO) {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + id));

        if (!existingPublisher.getName().equalsIgnoreCase(publisherDTO.getName()) &&
                publisherRepository.existsByNameIgnoreCase(publisherDTO.getName())) {
            throw new RuntimeException("Publisher name already exists: " + publisherDTO.getName());
        }

        existingPublisher.setName(publisherDTO.getName());
        existingPublisher.setAddress(publisherDTO.getAddress());
        existingPublisher.setWebsite(publisherDTO.getWebsite());

        Publisher updatedPublisher = publisherRepository.save(existingPublisher);
        return convertToDTO(updatedPublisher);
    }

    @Transactional
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + id));

        if (!publisher.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete publisher with associated books");
        }

        publisherRepository.delete(publisher);
    }

    private PublisherDTO convertToDTO(Publisher publisher) {
        PublisherDTO dto = new PublisherDTO();
        dto.setPublisherId(publisher.getPublisherId());
        dto.setName(publisher.getName());
        dto.setAddress(publisher.getAddress());
        dto.setWebsite(publisher.getWebsite());
        dto.setCreatedAt(publisher.getCreatedAt());
        dto.setBookCount((long) publisher.getBooks().size());
        return dto;
    }

    private Publisher convertToEntity(PublisherDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setAddress(dto.getAddress());
        publisher.setWebsite(dto.getWebsite());
        return publisher;
    }
}