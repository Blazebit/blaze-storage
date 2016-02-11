package com.blazebit.storage.core.model.jpa.hibernate;

import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.source.spi.AttributePath;

public class CustomImplicitNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    private static final long serialVersionUID = 1L;

    @Override
    protected String transformEntityName(EntityNaming entityNaming) {
        String entityName = super.transformEntityName(entityNaming);
		return addUnderscores(entityName);
    }

    @Override
	protected String transformAttributePath(AttributePath attributePath) {
		String attributeName= super.transformAttributePath(attributePath);
		return addUnderscores(attributeName);
	}
    
    private static String addUnderscores(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 10);
        
        for (int i = 0; i < s.length(); i++) {
            final char c = s.charAt(i);
            
            if (i != 0 && Character.isUpperCase(c)) {
                sb.append('_');
            }
            
            sb.append(Character.toUpperCase(c));
        }
        
        return sb.toString();
    }

}
