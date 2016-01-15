package com.epam.freelancer.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ApplicationContext {
	private Map<String, Object> beans = new ConcurrentHashMap<>();

	private ApplicationContext() {
	}

    public static ApplicationContext getInstance() {
        return ApplicationContextHolder.INSTANCE;
    }

	private void initContext() {
		initDAO();

	}

	private void initDAO() {
	}

	public Object getBean(String key) {
		return beans.get(key);
	}

	public Object addBean(String key, Object bean) {
		return beans.put(key, bean);
    }

    private static final class ApplicationContextHolder {
        private static final ApplicationContext INSTANCE = new ApplicationContext();

        static {
            INSTANCE.initContext();
        }
    }

}
