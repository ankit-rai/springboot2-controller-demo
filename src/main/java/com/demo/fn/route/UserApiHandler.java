package com.demo.fn.route;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.service.UserService;
import com.demo.fn.web.model.ResourceDetail;

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
        final ResourceDetail resourceDetail = (ResourceDetail) request.attribute("RESOURCE_DETAIL").orElse(null);
        logger.info("ResourceDetail from request -> {}", resourceDetail);
        
        final String userId = request.pathVariables().get("id");
        return Mono.subscriberContext()
                .flatMap(context -> {
                    final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                    
                    logger.info("[UserApiHandler#getById][TxId: {}] Fetching a user for given ID: '{}'", requestTxContext.getTxId(), userId);
                    
                    return userService.getById(userId)
                            .doOnSuccess(user -> logger.info("[TxId: {}] [UserApiHandler#getById] User with id '{}' was fetched successfully.", requestTxContext.getTxId(), userId));
                    })
                // If we want to pass additional context info to downstream layer
                .subscriberContext(ctx -> ctx.put("HandlerClassName", CLASS_NAME))
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(user))
                 )
                ;

    }

    public Mono<ServerResponse> addUser(final ServerRequest request) {

        return Mono.empty();
    }
}
