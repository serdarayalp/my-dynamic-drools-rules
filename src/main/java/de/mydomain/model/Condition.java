package de.mydomain.model;

import java.util.HashMap;
import java.util.Map;

public class Condition {

    private String field;
    private Object value;
    private Condition.Operator operator;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Condition.Operator getOperator() {
        return operator;
    }

    public void setOperator(Condition.Operator operator) {
        this.operator = operator;
    }

    public enum Operator {

        NOT_EQUAL_TO("NOT_EQUAL_TO"),
        EQUAL_TO("EQUAL_TO"),
        GREATER_THAN("GREATER_THAN"),
        LESS_THAN("LESS_THAN"),
        GREATER_THAN_OR_EQUAL_TO("GREATER_THAN_OR_EQUAL_TO"),
        LESS_THAN_OR_EQUAL_TO("LESS_THAN_OR_EQUAL_TO");

        private final String value;
        private static final Map<String, Operator> operatorsMap = new HashMap<>();

        Operator(String value) {
            this.value = value;
        }

        static {
            for (Condition.Operator op : values()) {
                operatorsMap.put(op.value, op);
            }
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Condition.Operator fromValue(String value) {
            Condition.Operator operator = operatorsMap.get(value);
            if (operator == null) {
                throw new IllegalArgumentException(value);
            } else {
                return operator;
            }
        }
    }
}
