package com.blazebit.storage.rest.model.multipart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.AbstractMultivaluedMap;
import javax.ws.rs.core.MultivaluedMap;

public class CaseInsensitiveMultivaluedMap<V> extends AbstractMultivaluedMap<String, V> implements Serializable {

	private static final long serialVersionUID = -123581638193222505L;
	
	public static final Comparator<String> CASE_INSENSITIVE_ORDER = new CaseInsensitiveComparator();

	private static class CaseInsensitiveComparator implements Comparator<String>, java.io.Serializable {

		private static final long serialVersionUID = -961338028855960714L;

		public int compare(String s1, String s2) {
			if (s1 == s2)
				return 0;
			int n1 = 0;
			// null check is different than JDK version of this method
			if (s1 != null)
				n1 = s1.length();
			int n2 = 0;
			if (s2 != null)
				n2 = s2.length();
			int min = Math.min(n1, n2);
			for (int i = 0; i < min; i++) {
				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				if (c1 != c2) {
					c1 = Character.toLowerCase(c1);
					c2 = Character.toLowerCase(c2);
					if (c1 != c2) {
						// No overflow because of numeric promotion
						return c1 - c2;
					}
				}
			}
			return n1 - n2;
		}
	}

	public CaseInsensitiveMultivaluedMap() {
		super(new TreeMap<String, List<V>>(CASE_INSENSITIVE_ORDER));
	}

	public CaseInsensitiveMultivaluedMap(MultivaluedMap<String, ? extends V> map) {
		this();
		putAll(map);
	}

	private <U extends V> void putAll(MultivaluedMap<String, U> map) {
		for (Entry<String, List<U>> e : map.entrySet()) {
			store.put(e.getKey(), new ArrayList<V>(e.getValue()));
		}
	}

	public CaseInsensitiveMultivaluedMap(Map<String, ? extends V> map) {
		this();
		for (Entry<String, ? extends V> e : map.entrySet()) {
			this.putSingle(e.getKey(), e.getValue());
		}
	}
}
