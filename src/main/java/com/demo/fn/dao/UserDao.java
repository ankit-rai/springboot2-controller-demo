package com.demo.fn.dao;

import com.demo.exception.ExceptionSpitter;
import com.demo.fn.exception.ExceptionCodes;
import com.demo.fn.exception.ExceptionMessages;
import com.demo.fn.model.User;
import com.demo.util.AppSupportFunctions;
import com.demo.util.InMemoryKeyValueStore;
import com.demo.util.logger.KeyValueLogger;
import com.demo.util.logger.LoggerUtilFunctions;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Component("userDao")
public class UserDao {
    
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	public static final String CLASS_NAME = UserDao.class.getCanonicalName();
	
	private static final InMemoryKeyValueStore kvStore = InMemoryKeyValueStore.INSTANCE;
	
	public UserDao() {
		User user = new User();
		user.setId("U1");
		user.setFirstName("U1_First");
		user.setLastName("U1_Last");
		kvStore.put(user.getId(), user);
		
		user = new User();
		user.setId("U2");
		user.setFirstName("U2_First");
		user.setLastName("U2_Last");
		kvStore.put(user.getId(), user);
		
		user = new User();
		user.setId("U3");
		user.setFirstName("U3_First");
		user.setLastName("U3_Last");
		kvStore.put(user.getId(), user);
		
		user = new User();
		user.setId("U4");
		user.setFirstName("U4_First");
		user.setLastName("U4_Last");
		kvStore.put(user.getId(), user);
		
		user = new User();
		user.setId("U5");
		user.setFirstName("U5_First");
		user.setLastName("U5_Last");
		kvStore.put(user.getId(), user);
	}
	
//	public Mono<User> getById(final String id) {
//		return Mono.subscriberContext()
//		        .flatMap(context -> {
//		            final RequestTxContext requestTxContext = context.get(RequestTxContext.CLASS_NAME);
//		            logger.info("[UserDao#getById] Context: {}", context);
	                
//		            this.aSampleMethod()
	                    // This is how you pass current context to a private method.
//		                .subscriberContext(innerCtx -> innerCtx.put(RequestTxContext.CLASS_NAME, requestTxContext))
//		                .block();
//		            
//		            return fetchUser(id)
//		                    .doOnSuccess(user -> logger.info("[TxId: {}] [UserDao#getById] User with id '{}' was fetched successfully.", requestTxContext.getTxId(), id))
//		                    ;
//		        })
//		    ;
//	}
	
	public Mono<User> getById(final String id) {
	    // Shows how to use log consumer. Signal object is passed automatically.
	    return fetchUser(id)
                .doOnEach(
                    new KeyValueLogger(logger)
                        .addTxPath(LoggerUtilFunctions.FN_TX_PATH_BUILDER.apply(CLASS_NAME, "getById"))
                        .add("MethodArguments", "[" + id + "]")
                        .consumeLog() 
                );
    }
	
	private Mono<User> fetchUser(final String id) {
	    // Simulate exception
	    if (StringUtils.equals(id, "U-1")) {
            return Mono.error(new RuntimeException("Throwing RuntimeException"));
        } else if (StringUtils.equals(id, "U-2")) {
            return Mono.error(ExceptionSpitter.spitDefault());
        } 
	    
	    final User user = (User) kvStore.get(id);
        if (user == null) {
            return ExceptionSpitter
                .forErrorCode(ExceptionCodes.REST_404001)
                .withErrorMessage(AppSupportFunctions.FN_FORMAT_STRING.apply(ExceptionMessages.REST_404001, new String[] {id}))
                .spitAsMono();
        }
        
        return Mono.just(user);
	}
	
//	private Mono<Void> aSampleMethod() {
//	    return Mono.subscriberContext()
//            .flatMap(context -> {
//                logger.info("[UserDao#aSampleMethod] Context --> {}", context);
//                return Mono.empty();
//            })
//        ;
//	}
}
