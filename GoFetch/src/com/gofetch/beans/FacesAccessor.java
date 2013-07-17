package com.gofetch.beans;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;

/**
 * Utility class for assigning ajax action controllers to programmatically created components
 * @author  Oleg Varaksin
 * see: http://www.javacodegeeks.com/2012/04/5-useful-methods-jsf-developers-should.html
 */

/*
 * Example use:
 * MenuItem mi = new MenuItem();
mi.setAjax(true);
mi.setValue(...);
mi.setProcess(...);
mi.setUpdate(...);
mi.setActionExpression(FacesAccessor.createMethodExpression(
    "#{navigationContext.setBreadcrumbSelection}", String.class, new Class[] {}));

UIParameter param = new UIParameter();
param.setId(...);
param.setName(...);
param.setValue(...);
mi.getChildren().add(param);
 * 
 */

public class FacesAccessor {

	public static MethodExpression createMethodExpression(
			String valueExpression, Class<?> expectedReturnType,
			Class<?>[] expectedParamTypes) {
		MethodExpression methodExpression = null;
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExpressionFactory factory = fc.getApplication()
					.getExpressionFactory();
			methodExpression = factory.createMethodExpression(
					fc.getELContext(), valueExpression, expectedReturnType,
					expectedParamTypes);
		} catch (Exception e) {
			throw new FacesException("Method expression '" + valueExpression
					+ "' could not be created.");
		}

		return methodExpression;
	}

	public static MethodExpressionActionListener createMethodActionListener(
			String valueExpression, Class<?> expectedReturnType,
			Class<?>[] expectedParamTypes) {
		MethodExpressionActionListener actionListener = null;
		try {
			actionListener = new MethodExpressionActionListener(
					createMethodExpression(valueExpression, expectedReturnType,
							expectedParamTypes));
		} catch (Exception e) {
			throw new FacesException("Method expression for ActionListener '"
					+ valueExpression + "' could not be created.");
		}

		return actionListener;
	}

}
