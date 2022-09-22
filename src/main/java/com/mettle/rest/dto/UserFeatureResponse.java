package com.mettle.rest.dto;

import com.mettle.model.Feature;

import java.util.Set;

public record UserFeatureResponse(Set<Feature> globallyEnabled, Set<Feature> userEnabled) {
}
