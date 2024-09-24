package com.example.resourcereservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.resourcereservation.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
