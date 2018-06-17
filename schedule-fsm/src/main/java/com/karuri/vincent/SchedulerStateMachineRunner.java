package com.karuri.vincent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchedulerStateMachineRunner {


    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"Beans.xml"});
        SchedulerStateMachineExecutor executor = (SchedulerStateMachineExecutor) context.getBean("stateMachineExecutor");
        executor.run();
    }
}
