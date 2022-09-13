package com.mettle.transactionwrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TransactionWrapper {

    private final EntityManager entityManager;

    @Transactional(timeout = 30, propagation = Propagation.REQUIRED)
    public void runInNewTransaction(Consumer<EntityManager> consumer) {
        consumer.accept(entityManager);
    }

    @Transactional(timeout = 30, propagation = Propagation.REQUIRED)
    public <T> T runFunctionInNewTransaction(Function<EntityManager, T> function) {
        return function.apply(entityManager);
    }
}
