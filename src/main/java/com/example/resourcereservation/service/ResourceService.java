package com.example.resourcereservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resourcereservation.model.Resource;
import com.example.resourcereservation.repository.ResourceRepository;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> getAllResources(){
        return resourceRepository.findAll();
    }

    public Resource getResourceById(Long id){
        return resourceRepository.findById(id).orElse(null);
    }

    public Resource saveResource(Resource resource){
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id){
        resourceRepository.deleteById(id);
    }
}
