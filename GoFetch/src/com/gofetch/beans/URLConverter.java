package com.gofetch.beans;

import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.gofetch.entities.URL;

@FacesConverter(value = "URLConverter")
public class URLConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value)  {
        if (value.trim().equals("")) {
            return null;
        } else {
            try {
            	//TODO: not too sure about this..... how to implement the URL version... ???
//                //int number = Integer.parseInt(value);
//
//                for (URL p : playerDB) {
//                    if (p.getNumber() == number) {
//                        return p;
//                    }
//                }

            } catch(NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		 if (value == null || value.equals("")) {
	            return "";
	        } else {
	            return ((URL) value).getUrl_address();
	        }
	}

}
