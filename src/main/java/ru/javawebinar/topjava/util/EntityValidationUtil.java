package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class EntityValidationUtil {

    private static final Logger log = getLogger(EntityValidationUtil.class);

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(AbstractBaseEntity entity) {
        Set<ConstraintViolation<AbstractBaseEntity>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            violations.forEach(v -> log.info(v.getMessage()));
            throw new ConstraintViolationException(violations);
        }
    }
}
