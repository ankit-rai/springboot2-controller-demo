package com.demo.fn.service;

import com.demo.fn.context.RequestTxContext;
import com.demo.fn.dao.UserDao;
import com.demo.fn.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 *
 * @author Niranjan Nanda
 */
@Component("userService")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public static final String CLASS_NAME = UserService.class.getCanonicalName();

    private final UserDao userDao;
    
    @Autowired
    public UserService(@Qualifier("userDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public Mono<User> getById(final String id) {
        return Mono.subscriberContext()
                .flatMap(context -> {
                    final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
                    return userDao.getById(id)
                            .doOnSuccess(user -> logger.info("[TxId: {}] [UserService] User with id '{}' was fetched successfully.", requestTxContext.getTxId(), id))
                            ;
                })
        ;
    }
}
