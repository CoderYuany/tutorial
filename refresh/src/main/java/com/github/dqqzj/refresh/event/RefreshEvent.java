//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.event;

import org.springframework.context.ApplicationEvent;

public class RefreshEvent extends ApplicationEvent {
    private Object event;
    private String eventDesc;

    public RefreshEvent(Object source, Object event, String eventDesc) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
    }

    public Object getEvent() {
        return this.event;
    }

    public String getEventDesc() {
        return this.eventDesc;
    }
}
