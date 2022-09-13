package com.mettle.repository;

import com.mettle.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    Set<Feature> findByEnabledTrue();

    Optional<Feature> findByName(String name);
}
