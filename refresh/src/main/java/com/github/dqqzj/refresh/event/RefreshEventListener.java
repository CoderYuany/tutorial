//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.dqqzj.refresh.event;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.dqqzj.refresh.ContextRefresher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class RefreshEventListener {
    private static Log log = LogFactory.getLog(RefreshEventListener.class);
    private ContextRefresher refresh;
    private AtomicBoolean ready = new AtomicBoolean(false);

    public RefreshEventListener(ContextRefresher refresh) {
        this.refresh = refresh;
    }

    @EventListener
    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    @EventListener
    public void handle(RefreshEvent event) {
        if (this.ready.get()) {
            log.debug("Event received " + event.getEventDesc());
            Set<String> keys = this.refresh.refresh();
            log.info("Refresh keys changed: " + keys);
        }

    }
}
