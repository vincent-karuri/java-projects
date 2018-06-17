package com.karuri.vincent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component("stateMachineExecutor")
public class SchedulerStateMachineExecutor {
    @Autowired
    private StateMachine<String, String> stateMachine;

    public void run() {
        stateMachine.start();

        // transition to S1 after E1
        System.out.println("State before: " + stateMachine.getState().getId());
        stateMachine.sendEvent("E1");
        System.out.println("State after: " + stateMachine.getState().getId());

        // transition to S2 after E2
        System.out.println("State before: " + stateMachine.getState().getId());
        stateMachine.sendEvent("E2");
        System.out.println("State after: " + stateMachine.getState().getId());
    }
}
