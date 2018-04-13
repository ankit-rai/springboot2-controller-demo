package com.demo.fn.service;

import com.demo.fn.dao.UserDao;
import com.demo.fn.model.User;
import com.demo.util.AppSupportFunctions;
import com.demo.util.logger.KeyValueLogger;
import com.demo.util.logger.LoggerUtilFunctions;

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
        /*
         * Shows how to use log consumer code along with signal object. 
         * Here, we have to call accept() explicitly on the consumer
         * (as usual with other functional code).
         */
        return userDao.getById(id)
            .doOnEach(signal ->
                new KeyValueLogger(logger)
                    .addTxPath(LoggerUtilFunctions.FN_TX_PATH_BUILDER.apply(CLASS_NAME, "getById"))
                    .add("MethodArguments", "[" + id + "]")
                    .add("Message", 
                            signal.hasError() 
                                ? AppSupportFunctions.FN_FORMAT_STRING.apply("Failed to fetch a user with id '%s'.", new String[] {id})
                                : AppSupportFunctions.FN_FORMAT_STRING.apply("User with id '%s' was fetched successfully.", new String[] {id})
                     )
                    .<User>consumeLog()
                    .accept(signal)
            )
        ;
    }
}
