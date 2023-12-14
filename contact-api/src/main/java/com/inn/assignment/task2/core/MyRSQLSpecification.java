package com.inn.assignment.task2.core;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.github.tennaito.rsql.misc.ArgumentFormatException;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyRSQLSpecification<T> implements Specification<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private String property;
	private transient ComparisonOperator operator;
	private List<String> arguments;

	public MyRSQLSpecification(String property, ComparisonOperator operator, List<String> arguments) {
		this.property = property;
		this.operator = operator;
		this.arguments = arguments;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Path<String> path = Utils.parseRootProperty(root, property);
		List<Object> args = parseArguments(path);
		Object argument = args.get(0);
		String actualArgument = getActualArgument();
		log.info(
				"Inside @Class: MyRsqlSpecification @Method: toPredicate @Param: property: {}, argument: {}, actualArgument: {}",
				property, argument, actualArgument);
		switch (MyRSQLSearchOperation.getSimpleOperator(operator)) {
		case EQUAL: {
			return getEqualConditionData(builder, path, argument, actualArgument);
		}
		case NOT_EQUAL: {
			return getNotEqualConditionData(builder, path, argument, actualArgument);
		}
		case GREATER_THAN: {
			return getGreaterThanConditionData(builder, path, argument);
		}
		case GREATER_THAN_OR_EQUAL: {
			return getGreaterThanOrEqualToConditionData(builder, path, argument);
		}
		case LESS_THAN: {
			return getLessThanConditionData(builder, path, argument);
		}
		case LESS_THAN_OR_EQUAL: {
			return getLessThanOrEqualToConditionData(builder, path, argument);
		}
		case IN:
			return path.in(args);
		case NOT_IN:
			return builder.not(path.in(args));
		default:
			return null;
		}
	}

	/**
	 * @SuppressWarnings({"unchecked", "rawtypes"}) private Path<String>
	 * parseRootProperty(Root<T> root) { Path path; if (property.contains(".")) {
	 * String[] pathSteps = property.split("\\."); String step = pathSteps[0]; path
	 * = root.get(step); for (int i = 1; i <= pathSteps.length - 1; ++i) { path =
	 * path.get(pathSteps[i]); } } else { path = root.get(property); } return path;
	 * }
	 **/

	@SuppressWarnings({ "unchecked" })
	private List<Object> parseArguments(Path<?> path) {
		Class<?> type = path.getJavaType();
		return (List<Object>) parse(arguments, type);
	}

	@SuppressWarnings("hiding")
	private <T> List<T> parse(List<String> arguments, Class<T> type)
			throws ArgumentFormatException, IllegalArgumentException {
		List<T> castedArguments = new ArrayList<>(arguments.size());
		for (String argument : arguments) {
			castedArguments.add(parse(argument, type));
		}
		return castedArguments;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "hiding" })
	private <T> T parse(String argument, Class<T> type) throws ArgumentFormatException, IllegalArgumentException {
		if (argument == null || "null".equalsIgnoreCase(argument.trim())) {
			return (T) null;
		}
		String actualArgument = argument;
		if ((type.equals(Integer.class) || type.equals(int.class) || type.equals(Float.class)
				|| type.equals(float.class) || type.equals(Double.class) || type.equals(double.class)
				|| type.equals(Long.class) || type.equals(long.class) || type.equals(Byte.class)
				|| type.equals(byte.class) || type.equals(BigDecimal.class) || type.isEnum())
				&& argument.startsWith("*") && argument.endsWith("*")) {
			argument = argument.replace("*", "");
		}
		try {
			if (type.equals(String.class))
				return (T) argument;
			if (type.equals(Integer.class) || type.equals(int.class))
				return (T) Integer.valueOf(argument);
			if (type.equals(Float.class) || type.equals(float.class))
				return (T) Float.valueOf(argument);
			if (type.equals(Double.class) || type.equals(double.class))
				return (T) Double.valueOf(argument);
			if (type.equals(Long.class) || type.equals(long.class))
				return (T) Long.valueOf(argument);
			if (type.equals(Byte.class) || type.equals(byte.class))
				return (T) Byte.valueOf(argument);
			if (type.equals(BigDecimal.class))
				return (T) new BigDecimal(argument);
			if (type.equals(Boolean.class) || type.equals(boolean.class))
				return (T) Boolean.valueOf(argument);
			if (type.isEnum() && actualArgument.startsWith("*") && actualArgument.endsWith("*"))
				return (T) argument;
			/**
			 * if (type.isEnum()) return (T) Enum.valueOf((Class<Enum>) type, argument);
			 **/
			if (type.isEnum()) {
				for (Enum value : ((Class<Enum>) type).getEnumConstants()) {
					if (value.name().compareToIgnoreCase(argument) == 0) {
						return (T) value;
					}
				}
			}
		} catch (IllegalArgumentException ex) {
			throw new ArgumentFormatException(argument, type);
		}
		if (type.equals(Date.class)) {
			return (T) parseDate(argument, type);
		}
		return returnFinalArgs(argument, type);
	}

	@SuppressWarnings("hiding")
	private <T> Date parseDate(String argument, Class<T> type) {
		log.info("Going For DateTime");
		try {
			return new SimpleDateFormat(DATE_TIME_PATTERN).parse(argument);
		} catch (ParseException ex) {
			log.info("Going For Date");
		}
		try {
			return new SimpleDateFormat(DATE_PATTERN).parse(argument);
		} catch (ParseException ex) {
			log.info("Going For Long To Date");
		}
		try {
			return new Date(Long.parseLong(argument));
		} catch (Exception ex) {
			log.info("Date Parsing Error");
			throw new ArgumentFormatException(argument, type);
		}
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private <T> T returnFinalArgs(String argument, Class<T> type) {
		try {
			Method method = type.getMethod("valueOf", String.class);
			return (T) method.invoke(type, argument);
		} catch (InvocationTargetException ex) {
			throw new ArgumentFormatException(argument, type);
		} catch (ReflectiveOperationException ex) {
			throw new IllegalArgumentException("Can Not Parse Argument Type: " + type);
		}
	}

	private String getActualArgument() {
		List<String> castedArgument = new ArrayList<>(arguments.size());
		for (String argument : arguments) {
			castedArgument.add(argument);
		}
		return castedArgument.get(0);
	}

	private Predicate getEqualConditionData(CriteriaBuilder builder, Path<String> path, Object argument,
			String actualArgument) {
		Class<?> type = path.getJavaType();
		if ((type.equals(Integer.class) || type.equals(int.class) || type.equals(Float.class)
				|| type.equals(float.class) || type.equals(Double.class) || type.equals(double.class)
				|| type.equals(Long.class) || type.equals(long.class) || type.equals(Byte.class)
				|| type.equals(byte.class) || type.equals(BigDecimal.class) || type.isEnum())
				&& actualArgument.startsWith("*") && actualArgument.endsWith("*")) {
			Expression<String> filterKeyExp = path.getParentPath().get(Utils.getParameter(property)).as(String.class);
//      filterKeyExp = builder.lower(filterKeyExp); //vvv
			return builder.like(filterKeyExp, "%" + argument + "%");
		} else if (argument instanceof String && argument.toString().contains("*")) {
			return builder.like(path, argument.toString().replace('*', '%'));
		} else if (argument == null) {
			return builder.isNull(path);
		} else {
			return builder.equal(path, argument);
		}
	}

	/**
	 * private String getParameter() { String parameter = property; if
	 * (property.contains(".")) { String[] pathSteps = property.split("\\.");
	 * parameter = pathSteps[pathSteps.length - 1]; } return parameter; }
	 **/

	private Predicate getNotEqualConditionData(CriteriaBuilder builder, Path<String> path, Object argument,
			String actualArgument) {
		Class<?> type = path.getJavaType();
		if ((type.equals(Integer.class) || type.equals(int.class) || type.equals(Float.class)
				|| type.equals(float.class) || type.equals(Double.class) || type.equals(double.class)
				|| type.equals(Long.class) || type.equals(long.class) || type.equals(Byte.class)
				|| type.equals(byte.class) || type.equals(BigDecimal.class) || type.isEnum())
				&& actualArgument.startsWith("*") && actualArgument.endsWith("*")) {
			Expression<String> filterKeyExp = path.getParentPath().get(Utils.getParameter(property)).as(String.class);
//      filterKeyExp = builder.lower(filterKeyExp); //vvv
			return builder.notLike(filterKeyExp, "%" + argument + "%");
		} else if (argument instanceof String && argument.toString().contains("*")) {
			return builder.notLike(path, argument.toString().replace('*', '%'));
		} else if (argument == null) {
			return builder.isNotNull(path);
		} else {
			return builder.notEqual(path, argument);
		}
	}

	private Predicate getGreaterThanConditionData(CriteriaBuilder builder, Path<String> path, Object argument) {
		if (argument instanceof Date) {
			return builder.greaterThan(path.as(Date.class), (Date) argument);
		}else if(argument instanceof Integer) {
			return builder.greaterThan(path.as(Integer.class), (Integer) argument);
		}
		
		
		return builder.greaterThan(path, argument.toString());
	}

	private Predicate getGreaterThanOrEqualToConditionData(CriteriaBuilder builder, Path<String> path,
			Object argument) {
		if (argument instanceof Date) {
			return builder.greaterThanOrEqualTo(path.as(Date.class), (Date) argument);
		}else if(argument instanceof Integer) {
			return builder.greaterThanOrEqualTo(path.as(Integer.class), (Integer) argument);
		}
		return builder.greaterThanOrEqualTo(path, argument.toString());
	}

	private Predicate getLessThanConditionData(CriteriaBuilder builder, Path<String> path, Object argument) {
		if (argument instanceof Date) {
			return builder.lessThan(path.as(Date.class), (Date) argument);
		}else if(argument instanceof Integer) {
			return builder.lessThan(path.as(Integer.class), (Integer) argument);
		}
		return builder.lessThan(path, argument.toString());
	}

	private Predicate getLessThanOrEqualToConditionData(CriteriaBuilder builder, Path<String> path, Object argument) {
		if (argument instanceof Date) {
			return builder.lessThanOrEqualTo(path.as(Date.class), (Date) argument);
		}else if(argument instanceof Integer) {
			return builder.lessThanOrEqualTo(path.as(Integer.class), (Integer) argument);
		}
		return builder.lessThanOrEqualTo(path, argument.toString());
	}

}
