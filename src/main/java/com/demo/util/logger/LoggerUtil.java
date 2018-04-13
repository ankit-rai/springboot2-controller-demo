package com.demo.util.logger;

import com.demo.fn.context.RequestTxContext;

import java.util.function.Consumer;

import reactor.core.publisher.Signal;
import reactor.util.context.Context;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class LoggerUtil {
    
    public static final <T> Consumer<Signal<T>> log(final KeyValueLogger kvLogger) {
        return signal -> {
            kvLogger.targetClassLogger().info("[LoggerUtil#log] Caught Signal --> {}", signal);
            if (!(signal.isOnNext() || signal.isOnError())) {
              return;
            }

            final Context context = signal.getContext();
            
            if (signal.hasError()) {
                kvLogger.addException(signal.getThrowable());
                kvLogger.addTxStatus(TxStatus.FAILURE);
            } else {
                kvLogger.addTxStatus(TxStatus.SUCCESS);
            }

            kvLogger.targetClassLogger().info("[LoggerUtil#log] Context inside kv logger consumer --> {}", context);

            final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
            kvLogger.addTxId(requestTxContext.getTxId());
            kvLogger.log();
        };
    }
    
}
