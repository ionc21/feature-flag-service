package com.mettle.dto;

import com.mettle.model.Feature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class UserFeatureResponse {
    private Set<Feature> globallyEnabled;
    private Set<Feature> userEnabled;
}
