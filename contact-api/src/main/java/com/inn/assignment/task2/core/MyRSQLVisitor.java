package com.inn.assignment.task2.core;

import org.springframework.data.jpa.domain.Specification;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class MyRSQLVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

	  private MyRSQLSpecBuilder<T> builder;

	  public MyRSQLVisitor() {
	    builder = new MyRSQLSpecBuilder<>();
	  }

	  @Override
	  public Specification<T> visit(AndNode node, Void param) {
	    return builder.createSpecification(node);
	  }

	  @Override
	  public Specification<T> visit(OrNode node, Void param) {
	    return builder.createSpecification(node);
	  }

	  @Override
	  public Specification<T> visit(ComparisonNode node, Void params) {
	    return builder.createSpecification(node);
	  }
}
