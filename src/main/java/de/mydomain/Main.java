package de.mydomain;

import de.mydomain.model.*;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main {

    static public void main(String[] args) throws Exception {

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setPrice(5000.1);
        orderEvent.setCustomer("Mein Unternehmen GmBh.");

        Rule rule = new Rule();


        // price > 5000.0
        Condition condition = new Condition();
        condition.setField("price");
        condition.setOperator(Condition.Operator.GREATER_THAN);
        condition.setValue(5000.0);

        // customer == "Mein Unternehmen GmBh."
        Condition condition2 = new Condition();
        condition2.setField("customer");
        condition2.setOperator(Condition.Operator.EQUAL_TO);
        condition2.setValue("Mein Unternehmen GmBh.");

        rule.setEventType(Rule.EventType.ORDER);

        rule.setConditions(Arrays.asList(condition, condition2));

        // DRL-Regel mit Hilfe des Templates erstellen
        String drl = applyRuleTemplate(orderEvent, rule);

        Alert alert = evaluate(drl, orderEvent);

        System.out.println("AlertStatus ist: " + alert.getAlert());
    }

    private static String applyRuleTemplate(Event event, Rule rule) {
        Map<String, Object> data = new HashMap<>();
        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();

        data.put("rule", rule);
        data.put("eventType", event.getClass().getName()); // de.mydomain.model.OrderEvent

        return objectDataCompiler.compile(
                List.of(data),
                Thread.currentThread().getContextClassLoader().getResourceAsStream("rules/rule-template.drl"));
    }

    private static Alert evaluate(String drl, Event event) {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("src/main/resources/rule.drl", drl);

        kieServices.newKieBuilder(kieFileSystem).buildAll();

        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        StatelessKieSession statelessKieSession = kieContainer.getKieBase().newStatelessKieSession();

        // Template erwartet eine Alert-Instanz (vermerkt mit "global", also "global Alert alert")
        Alert alert = new Alert();
        statelessKieSession.getGlobals().set("alert", alert);

        statelessKieSession.execute(event);

        return alert;
    }
}
