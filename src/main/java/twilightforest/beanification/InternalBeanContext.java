package twilightforest.beanification;

import java.lang.reflect.Field;

public class InternalBeanContext extends AbstractBeanContext {

    static InternalBeanContext INSTANCE = new InternalBeanContext();

    public static <T> T inject(Class<T> type) {
        return inject(type, null);
    }

    public static <T> T inject(Class<T> type, String name) {
        return INSTANCE.injectInternal(type, name);
    }

    static void injectInto(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InternalAutowired.class)) {
                InternalAutowired annotation = field.getAnnotation(InternalAutowired.class);
                String name = annotation.value();
                Object dependency = inject(field.getType(), name.isEmpty() ? null : name);
                if (dependency != null) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, dependency);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject @InternalAutowired field: " + field.getName(), e);
                    }
                }
            }
        }
    }

	@SuppressWarnings("unchecked")
	static <T> T injectInternal(Class<T> type) {
		return INSTANCE.injectInternal(type, null);
	}
}
