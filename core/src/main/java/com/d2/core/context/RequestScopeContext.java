package com.d2.core.context;

import java.util.HashMap;
import java.util.Map;

public class RequestScopeContext {
	private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

	public static void setAttribute(String key, Object value) {
		context.get().put(key, value);
	}

	public static Object getAttribute(String key) {
		return context.get().get(key);
	}

	public static void clear() {
		context.remove();
	}
}
