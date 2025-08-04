package supportly.supportlybackend.Criteria;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import supportly.supportlybackend.Annotation.OperatorSql;
import supportly.supportlybackend.Annotation.SpecField;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class GenericSpecificationBuilder<T> {

    public Specification<T> build(Object searchCriteria) {
        Specification<T> spec = Specification.where(null);

        Field[] fields = searchCriteria.getClass().getDeclaredFields();
        Set<String> processedBaseFields = new HashSet<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value;

            try {
                value = field.get(searchCriteria);
            } catch (IllegalAccessException e) {
                continue;
            }

            if (value == null || (value instanceof String str && str.isBlank())) {
                continue;
            }

            String fieldName = field.getName();

            if (fieldName.endsWith("From") || fieldName.endsWith("To")) {
                String baseField = fieldName.replaceAll("From|To", "");
                if (processedBaseFields.contains(baseField)) {
                    continue;
                }
                processedBaseFields.add(baseField);

                Object from = getValueByName(fields, searchCriteria, baseField + "From");
                Object to = getValueByName(fields, searchCriteria, baseField + "To");

                if (from == null && to == null) {
                    continue;
                }

                SpecField annotation = null;
                String path = baseField;
                try {
                    Field baseFieldRef = searchCriteria.getClass().getDeclaredField(baseField);
                    annotation = baseFieldRef.getAnnotation(SpecField.class);
                    if (annotation != null && !annotation.path().isEmpty()) {
                        path = annotation.path();
                    }
                } catch (NoSuchFieldException ignored) {
                }

                String fullPath = path;

                spec = spec.and((root, query, cb) -> {
                    Path<?> entityFieldPath = resolvePath(root, fullPath);

                    Class<?> fieldType = from != null ? from.getClass() : to.getClass();

                    @SuppressWarnings("unchecked")
                    Expression<? extends Comparable> comparableExpression = entityFieldPath.as((Class<? extends Comparable>) fieldType);

                    if (from != null && to != null) {
                        return cb.between(comparableExpression, (Comparable) from, (Comparable) to);
                    } else if (from != null) {
                        return cb.greaterThanOrEqualTo(comparableExpression, (Comparable) from);
                    } else {
                        return cb.lessThanOrEqualTo(comparableExpression, (Comparable) to);
                    }
                });

                continue;
            }

            SpecField annotation = field.getAnnotation(SpecField.class);
            String path = (annotation != null && !annotation.path().isEmpty()) ? annotation.path() : fieldName;
            OperatorSql operator = (annotation != null) ? annotation.operator() : OperatorSql.EQUAL;

            spec = spec.and((root, query, cb) -> {
                Path<?> entityFieldPath = resolvePath(root, path);

                return switch (operator) {
                    case EQUAL -> cb.equal(entityFieldPath, value);
                    case LIKE ->
                        cb.like(cb.lower(entityFieldPath.as(String.class)), cb.literal("%" + value.toString().toLowerCase() + "%"));
                    case GREATER_THAN -> cb.greaterThan(entityFieldPath.as(Comparable.class), (Comparable) value);
                    case LESS_THAN -> cb.lessThan(entityFieldPath.as(Comparable.class), (Comparable) value);
                    case GREATER_THAN_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(entityFieldPath.as(Comparable.class), (Comparable) value);
                    case LESS_THAN_OR_EQUAL ->
                            cb.lessThanOrEqualTo(entityFieldPath.as(Comparable.class), (Comparable) value);
                    default -> cb.equal(entityFieldPath, value);
                };
            });
        }

        return spec;
    }


    private Object getValueByName(Field[] fields, Object obj, String name) {
        for (Field f : fields) {
            if (f.getName().equals(name)) {
                f.setAccessible(true);
                try {
                    return f.get(obj);
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return null;
    }

    private Path<Object> resolvePath(From<?, ?> root, String path) {
        String[] parts = path.split("\\.");
        Path<?> result = root;

        for (String part : parts) {
            try {
                result = result.get(part);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Nie znaleziono pola: " + part + " w " + result.getJavaType(), e);
            }
        }

        return (Path<Object>) result;
    }
}

