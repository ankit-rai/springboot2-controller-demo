package com.demo.fn.route;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.service.UserService;
import com.demo.util.logger.KeyValueLogger;
import com.demo.util.logger.LoggerUtilFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 *
 * @author Niranjan Nanda
 */
@Component
public class UserApiHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserApiHandler.class);
    
    public static final String CLASS_NAME = UserApiHandler.class.getCanonicalName();

    private final UserService userService;
    
    @Autowired
    public UserApiHandler( @Qualifier("userService")final UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> getById(final ServerRequest request) {
        final String userId = request.pathVariables().get("id");
        
        // Shows how to use the logger consumer inside a Mono.subscriberContext() code.
        return Mono.subscriberContext()
                .flatMap(context -> {
                    logger.info("[UserApiHandler#getById] Request Context --> {}", context);
                    
                    final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                    
                    return userService.getById(userId)
                            .doOnEach(new KeyValueLogger(logger)
                                        .addTxPath(LoggerUtilFunctions.FN_TX_PATH_BUILDER.apply(CLASS_NAME, "getById"))
                                        .consumeLog())
                            .flatMap(user -> ServerResponse
                                    .ok()
                                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                                    .header("X-Trace-Id", requestTxContext.getTxId())
                                    .body(BodyInserters.fromObject(user)))
                            ;
                });
    }

    public Mono<ServerResponse> addUser(final ServerRequest request) {
        return Mono.empty();
    }
}
