package com.mettle.rest.dto;

import javax.validation.constraints.NotEmpty;


public record UserFeatureEnableRequest(@NotEmpty String featureName, @NotEmpty String userName) {
}
