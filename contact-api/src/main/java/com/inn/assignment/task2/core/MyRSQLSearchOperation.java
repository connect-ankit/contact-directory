package com.inn.assignment.task2.core;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;

public enum MyRSQLSearchOperation {

	EQUAL(RSQLOperators.EQUAL), NOT_EQUAL(RSQLOperators.NOT_EQUAL), GREATER_THAN(RSQLOperators.GREATER_THAN),
	GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL), LESS_THAN(RSQLOperators.LESS_THAN),
	LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL), IN(RSQLOperators.IN), NOT_IN(RSQLOperators.NOT_IN);

	private ComparisonOperator operator;

	private MyRSQLSearchOperation(ComparisonOperator operator) {
		this.operator = operator;
	}

	public ComparisonOperator getOperator() {
		return this.operator;
	}

	public static MyRSQLSearchOperation getSimpleOperator(ComparisonOperator operator) {
		for (MyRSQLSearchOperation operation : values()) {
			if (operation.getOperator() == operator) {
				return operation;
			}
		}
		return null;
	}
}
