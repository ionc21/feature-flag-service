package com.mettle.rest;

import com.mettle.dto.FeatureCreateRequest;
import com.mettle.dto.UserFeatureEnableRequest;
import com.mettle.dto.UserFeatureResponse;
import com.mettle.model.Feature;
import com.mettle.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("feature_flags")
class FeatureFlagController {
    private final FeatureService featureService;

    @Operation(summary = "Enable feature for the requested user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User feature activated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping(value = "feature/enable", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    Boolean enable(@RequestBody @Valid UserFeatureEnableRequest userFeatureEnableRequest) {
        return featureService.enableFeature(userFeatureEnableRequest);
    }

    @Operation(summary = "Get all active global and user specific features")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User feature activated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping(value = "feature/enabled/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserFeatureResponse> getAllEnabledFeatures(@AuthenticationPrincipal @Parameter(hidden = true) User user) {
        return featureService.findAllGlobalAndUserEnabled(user.getUsername());
    }

    @Operation(summary = "Add new feature")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User feature activated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<Feature> addNewFeature(@RequestBody @Valid FeatureCreateRequest featureCreateRequest) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create(requireNonNull(fromCurrentRequest().build().getPath())));
        return new ResponseEntity<>(featureService.addNewFeature(featureCreateRequest), httpHeaders, CREATED);
    }
}
