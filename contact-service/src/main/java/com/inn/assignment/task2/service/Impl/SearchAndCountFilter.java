package com.inn.assignment.task2.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.inn.assignment.task2.core.BusinessException;
import com.inn.assignment.task2.core.Constant.ResponseKey;
import com.inn.assignment.task2.core.GenericResponse;
import com.inn.assignment.task2.core.MyRSQLVisitor;
import com.inn.assignment.task2.service.ISearchAndCountFilter;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.val;

@Service
public class SearchAndCountFilter implements ISearchAndCountFilter {

	@PersistenceContext(name = "DEFAULT", type = PersistenceContextType.TRANSACTION)
	private EntityManager em;

	private String extraCriteria = null;

	@Override
	public void addCriteria(String extraCriteria) {
		this.extraCriteria = extraCriteria;
	}

	private String createCriteriaFiql(String query) {
		if (this.extraCriteria != null) {
			if (query == null) {
				query = "";
			}
			query = query + this.extraCriteria;
		}
		return query;
	}

	@Override
	public GenericResponse search(Class<?> entity, String query, String orderBy, String orderType, Integer llimit,
			Integer ulimit) {
		JSONObject data;
		query = createCriteriaFiql(query);
		this.extraCriteria = null;
		if (llimit == null || ulimit == null) {
			llimit = 0;
			ulimit = 500;
		}
		data = searchByFilter(entity, query, orderBy, orderType, llimit, ulimit);
		return getResponse(llimit, ulimit, data);
	}

	private JSONObject searchByFilter(Class<?> entity, String query, String orderBy, String orderType, Integer llimit,
			Integer ulimit) {
		val queryObject = new JSONObject();
		try {
			val builder = em.getCriteriaBuilder();
			val searchCriteria = builder.createQuery(entity);
			val countCriteria = builder.createQuery(Long.class);
			val root = searchCriteria.from(entity);
			countCriteria.from(entity);
			getRootNode(query, builder, searchCriteria, countCriteria, root);
			getOrderList(orderBy, orderType, builder, searchCriteria, root);
			val searchQuery = em.createQuery(searchCriteria);
			if (llimit != null && ulimit != null && ulimit > 0) {
				searchQuery.setFirstResult(llimit).setMaxResults(ulimit - llimit + 1);
			}
			queryObject.put(ResponseKey.DATA, searchQuery.getResultList());
			return queryObject;
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getRootNode(String query, final CriteriaBuilder builder, final CriteriaQuery<?> searchCriteria,
			final CriteriaQuery<Long> countCriteria, final Root<?> root) {
		Node rootNode;
		if (StringUtils.isNotEmpty(query)) {
			rootNode = new RSQLParser().parse(query);
			Specification specification = rootNode.accept(new MyRSQLVisitor<>());
			Predicate predicate = specification.toPredicate(root, searchCriteria, builder);
			Predicate countPredicate = specification.toPredicate(root, countCriteria, builder);
			countCriteria.select(builder.countDistinct(root));
			searchCriteria.where(predicate);
			countCriteria.where(countPredicate);
		} else {
			countCriteria.select(builder.countDistinct(root));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static jakarta.persistence.criteria.Path<String> parseRootProperty(Root<?> root, String property) {
		Path path;
		if (property.contains(".")) {
			String[] pathSteps = property.split("\\.");
			String step = pathSteps[0];
			path = root.get(step);
			for (int i = 1; i <= pathSteps.length - 1; ++i) {
				path = path.get(pathSteps[i]);
			}
		} else {
			path = root.get(property);
		}
		return path;
	}

	public static String getParameter(String property) {
		String parameter = property;
		if (property.contains(".")) {
			String[] pathSteps = property.split("\\.");
			parameter = pathSteps[pathSteps.length - 1];
		}
		return parameter;
	}

	private void getOrderList(String orderBy, String orderType, final CriteriaBuilder builder,
			final CriteriaQuery<?> searchCriteria, final Root<?> root) {
		orderType = orderType != null ? orderType.toLowerCase() : "desc";
		val orderList = new ArrayList<Order>();
		List<String> orderByLists = new ArrayList<>();
		if (orderBy != null) {
			orderByLists = Arrays.asList(orderBy.split(","));
		}
		for (val item : orderByLists) {
			Path<String> path = parseRootProperty(root, item);
			Expression<String> filterKeyExp = path.getParentPath().get(getParameter(item));
			if ("desc".equals(orderType)) {
				orderList.add(builder.desc(filterKeyExp));
			} else {

				orderList.add(builder.asc(filterKeyExp));
			}
		}
		searchCriteria.orderBy(orderList);
	}

	@Override
	public GenericResponse getResponse(Integer llimit, Integer ulimit, JSONObject data) {
		return GenericResponse.ok().setData(data.get(ResponseKey.DATA)).build();
	}

}
