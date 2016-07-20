package util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class SimpleFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getLoggerName().contains("Transaction")) {
            return FilterReply.ACCEPT;
        } else if (iLoggingEvent.getLevel().equals(Level.ERROR) ){
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }
}
