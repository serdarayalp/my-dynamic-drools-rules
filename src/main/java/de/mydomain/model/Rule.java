package de.mydomain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rule {

    private List<Condition> conditions;
    private EventType eventType;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        StringBuilder statementBuilder = new StringBuilder();

        for (Condition condition : getConditions()) {

            String operator = null;

            switch (condition.getOperator()) {
                case EQUAL_TO:
                    operator = "==";
                    break;
                case NOT_EQUAL_TO:
                    operator = "!=";
                    break;
                case GREATER_THAN:
                    operator = ">";
                    break;
                case LESS_THAN:
                    operator = "<";
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    operator = ">=";
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    operator = "<=";
                    break;
            }

            statementBuilder.append(condition.getField()).append(" ").append(operator).append(" ");

            if (condition.getValue() instanceof String) {
                statementBuilder.append("'").append(condition.getValue()).append("'");
            } else {
                statementBuilder.append(condition.getValue());
            }

            statementBuilder.append(" && ");
        }

        String statement = statementBuilder.toString();

        // remove trailing &&
        return statement.substring(0, statement.length() - 4);
    }

    public enum EventType {

        ORDER("ORDER"),
        INVOICE("INVOICE");

        private final String value;
        private static final Map<String, EventType> eventTypeMap = new HashMap<>();

        static {
            for (EventType et : values()) {
                eventTypeMap.put(et.value, et);
            }
        }

        EventType(String value) {
            this.value = value;
        }

        public static EventType fromValue(String value) {
            EventType eventType = eventTypeMap.get(value);
            if (eventType == null) {
                throw new IllegalArgumentException(value);
            } else {
                return eventType;
            }
        }

    }
}
