package com.gofetch.beans;

// from: http://forum.primefaces.org/viewtopic.php?f=3&t=5344&p=26987#p26987

import java.util.HashMap;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.BehaviorEvent;
import org.primefaces.component.behavior.ajax.AjaxBehavior;

/**
 * 
 * @author alandonohoe
 * deals with adding ajax behaviour to programmatically created PF components, like treenodes...
 * - this at the moment does not seem to be working
 * //TODO: delete if we cant get this class to work - mostly used in FullScreebDashboard bean 
 */
public class MyAjaxBehavior extends AjaxBehavior {

	@Override
	public Object saveState(FacesContext context) {
		HashMap<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("update", getUpdate());
		map.put("process", getProcess());
		map.put("oncomplete", getOncomplete());
		map.put("onerror", getOnerror());
		map.put("onsuccess", getOnsuccess());
		map.put("onstart", getOnstart());
		map.put("listener", getListener());

		if (initialStateMarked())
			return null;
		return UIComponentBase.saveAttachedState(context, map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void restoreState(FacesContext context, Object state) {
		if (state != null) {
			HashMap<String, Object> map;
			map = (HashMap<String, Object>) UIComponentBase
					.restoreAttachedState(context, state);

			setUpdate((String) map.get("update"));
			setProcess((String) map.get("process"));
			setOncomplete((String) map.get("oncomplete"));
			setOnerror((String) map.get("onerror"));
			setOnsuccess((String) map.get("onsuccess"));
			setOnstart((String) map.get("onstart"));
			setListener((MethodExpression) map.get("listener"));
		}

	}

	@Override
	public void broadcast(BehaviorEvent event) throws AbortProcessingException {
		ELContext eLContext = FacesContext.getCurrentInstance().getELContext();

		// Backward compatible implementation of listener invocation
		if (getListener() != null) {
			try {
				getListener().invoke(eLContext, new Object[] { event });
			} catch (IllegalArgumentException exception) {
				getListener().invoke(eLContext, new Object[0]);
			}
		}
	}

}
