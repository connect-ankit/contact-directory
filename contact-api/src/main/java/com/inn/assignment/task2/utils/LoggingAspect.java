package com.inn.assignment.task2.utils;

import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	private ThreadLocal<String> traceId = new ThreadLocal<>();

	@Around("@annotation(com.inn.assignment.task2.utils.CustomLogger)")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger log = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());
		traceId.set(UUID.randomUUID().toString());
		String methodName = joinPoint.getSignature().getName();
		String declaringType = joinPoint.getSignature().getDeclaringTypeName();
		log.info("Trace ID: {} - Before method execution - class: {} method: {}", traceId.get(), declaringType,
				methodName);

		Object[] obj = joinPoint.getArgs();
		for (Object o : obj) {
			log.info("Trace ID: {} - Parameter: {}", traceId.get(), o);
		}

		Object returnValue = null;
		try {
			returnValue = joinPoint.proceed();
			String returnValueStr = (returnValue != null) ? returnValue.toString() : "null";
			log.info("Trace ID: {} - After method execution - class: {} method: {}", traceId.get(), declaringType,
					methodName);
			log.info("Trace ID: {} - After returning: {}", traceId.get(), returnValueStr);
			traceId.remove();
		} catch (Exception e) {
			log.error("Trace ID: {} - Exception thrown in method - class: {} method: {}", traceId.get(), declaringType,
					methodName, e);

			traceId.remove();
			throw e;
		}
		traceId.remove();
		return returnValue;
	}

}
