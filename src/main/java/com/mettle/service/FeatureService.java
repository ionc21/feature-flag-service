package com.mettle.service;

import com.mettle.model.Feature;
import com.mettle.model.User;
import com.mettle.repository.FeatureRepository;
import com.mettle.repository.UserRepository;
import com.mettle.rest.dto.UserFeatureResponse;
import com.mettle.transactionwrapper.TransactionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;
    private final TransactionWrapper transactionWrapper;

    public ResponseEntity<UserFeatureResponse> findAllGlobalAndUserEnabled(String userName) {
        return transactionWrapper.runFunctionInNewTransaction(entityManager -> getAllEnabledFeaturesForCurrentUser((userName)));
    }

    private ResponseEntity<UserFeatureResponse> getAllEnabledFeaturesForCurrentUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new EntityNotFoundException("User not found with name - " + userName));

        return ResponseEntity.ok(new UserFeatureResponse(featureRepository.findByEnabledTrue(), user.getFeatures()));
    }

    public Feature addNewFeature(String featureName) {
        return transactionWrapper
                .runFunctionInNewTransaction(entityManager -> featureRepository.save(Feature.builder()
                        .name(featureName)
                        .enabled(false)
                        .build()));
    }

    public Boolean enableFeature(String featureName, String userName) {
        transactionWrapper.runInNewTransaction(entityManager -> enableUserFeature(featureName, userName));
        return true;
    }

    private void enableUserFeature(String featureName, String userName) {
        Feature feature = featureRepository.findByName(featureName)
                .orElseThrow(() -> new EntityNotFoundException(format("Feature %s doesn't exist", featureName)));
        feature.setEnabled(true);
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new EntityNotFoundException(format("User %s doesn't exist", userName)));
        user.getFeatures().add(feature);
        userRepository.save(user);
    }
}
