package com.inn.assignment.task2.core;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.inn.assignment.task2.core.Constant.FIQL;
import com.inn.assignment.task2.utils.CustomLogger;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class Utils {

  private Utils() {}

  
  
  public static String getFiql(String query) {
		try {
			JSONObject fiqlJSONObject = new JSONObject(query);
			Map<String, String> operatorMap = new HashMap<>();
			operatorMap.put("AND", ";");
			operatorMap.put("OR", ",");
			Map<String, String> comparatorMap = new HashMap<>();
			comparatorMap.put("EQUAL", "==");
			comparatorMap.put("WILDCARD", "==");
			comparatorMap.put("NOT_EQUAL", "!=");
			comparatorMap.put("GREATER_THAN", "=gt=");
			comparatorMap.put("GREATER_THAN_OR_EQUAL", "=ge=");
			comparatorMap.put("LESS_THAN", "=lt=");
			comparatorMap.put("LESS_THAN_OR_EQUAL", "=le=");
			comparatorMap.put("INCLUDE", "=in=");
			comparatorMap.put("NOT_INCLUDE", "=out=");
			StringBuilder fiqlBuilder = getSearchFiql(fiqlJSONObject, operatorMap, comparatorMap);
			return fiqlBuilder.toString();
		} catch (Exception ex) {
			
			return null;
		}
	}
  
  
  @CustomLogger
	private static StringBuilder getSearchFiql(JSONObject fiqlJSONObject, Map<String, String> operatorMap,
			Map<String, String> comparatorMap) {
		StringBuilder fiqlBuilder = new StringBuilder();
		if (FIQL.CONSTRAINT.equals(fiqlJSONObject.optString(FIQL.TYPE))) {
			if (FIQL.WILDCARD.equals(fiqlJSONObject.optString(FIQL.COMPARISON))) {
				return fiqlBuilder.append(fiqlJSONObject.optString(FIQL.SELECTOR))
						.append(comparatorMap.get(fiqlJSONObject.optString(FIQL.COMPARISON))).append("'*")
						.append(fiqlJSONObject.opt(FIQL.ARGUMENT)).append("*'");
			} else if (FIQL.INCLUDE.equals(fiqlJSONObject.optString(FIQL.COMPARISON))
					|| FIQL.NOT_INCLUDE.equals(fiqlJSONObject.optString(FIQL.COMPARISON))) {
				return fiqlBuilder.append(fiqlJSONObject.optString(FIQL.SELECTOR))
						.append(comparatorMap.get(fiqlJSONObject.optString(FIQL.COMPARISON))).append("(")
						.append(getInComparatorValue(fiqlJSONObject.opt(FIQL.ARGUMENT))).append(")");
			} else {
				return fiqlBuilder.append(fiqlJSONObject.optString(FIQL.SELECTOR))
						.append(comparatorMap.get(fiqlJSONObject.optString(FIQL.COMPARISON))).append("'")
						.append(fiqlJSONObject.opt(FIQL.ARGUMENT)).append("'");
			}
		} else if (FIQL.COMBINATION.equals(fiqlJSONObject.optString(FIQL.TYPE))) {
			if (FIQL.AND.equals(fiqlJSONObject.optString(FIQL.OPERATOR))) {
				return getSearchFiql(fiqlJSONObject.getJSONObject(FIQL.LHS), operatorMap, comparatorMap)
						.append(operatorMap.get(fiqlJSONObject.getString(FIQL.OPERATOR)))
						.append(getSearchFiql(fiqlJSONObject.getJSONObject(FIQL.RHS), operatorMap, comparatorMap));
			} else if (FIQL.OR.equals(fiqlJSONObject.optString(FIQL.OPERATOR))) {
				return fiqlBuilder.append("((")
						.append(getSearchFiql(fiqlJSONObject.getJSONObject(FIQL.LHS), operatorMap, comparatorMap))
						.append(")").append(operatorMap.get(fiqlJSONObject.getString(FIQL.OPERATOR))).append("(")
						.append(getSearchFiql(fiqlJSONObject.getJSONObject(FIQL.RHS), operatorMap, comparatorMap))
						.append("))");
			} else {
				throw new BusinessException(FIQL.OPERATOR_IS_INCORRECT);
			}
		} else {
			throw new BusinessException(FIQL.FIQL_TYPE_IS_INCORRECT);
		}
	}

	
	private static String getInComparatorValue(Object object) {
		JSONArray argument = null;
		if (object instanceof JSONArray) {
			argument = (JSONArray) object;
		} else {
			argument = new JSONArray(object.toString());
		}
		StringBuilder inBuilder = new StringBuilder();
		for (Object value : argument) {
			if (inBuilder.length() == 0) {
				inBuilder.append("'").append(value).append("'");
			} else {
				inBuilder.append(",'").append(value).append("'");
			}
		}
		return inBuilder.toString();
	}
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Path<String> parseRootProperty(Root<?> root, String property) {
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
}
