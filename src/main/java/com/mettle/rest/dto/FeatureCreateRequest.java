package com.mettle.rest.dto;

import javax.validation.constraints.NotEmpty;

public record FeatureCreateRequest(@NotEmpty String featureName) {
}
