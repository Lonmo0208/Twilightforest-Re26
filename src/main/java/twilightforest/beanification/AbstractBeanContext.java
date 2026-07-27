package twilightforest.beanification;

import java.util.*;

public abstract class AbstractBeanContext {

    protected final Map<BeanDefinition<?>, Object> BEANS = new HashMap<>();
    private boolean frozen = false;

    protected void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen() {
        return frozen;
    }

    protected void registerInternal(Class<?> type, String name, Object instance) {
        BEANS.put(new BeanDefinition<>(type, name), instance);
    }

    @SuppressWarnings("unchecked")
    protected <T> T injectInternal(Class<T> type, String name) {
        BeanDefinition<?> targetDef = new BeanDefinition<>(type, name != null ? name : "");
        Object bean = BEANS.get(targetDef);
        if (bean != null) {
            return (T) bean;
        }
        // Try matching only by type if not found with name
        if (name != null && !name.isEmpty()) {
            for (Map.Entry<BeanDefinition<?>, Object> entry : BEANS.entrySet()) {
                if (entry.getKey().type().equals(type)) {
                    return (T) entry.getValue();
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> injectFuzzyInternal(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Map.Entry<BeanDefinition<?>, Object> entry : BEANS.entrySet()) {
            if (type.isAssignableFrom(entry.getKey().type())) {
                result.add((T) entry.getValue());
            }
        }
        return result;
    }
}
