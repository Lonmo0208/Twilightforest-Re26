package twilightforest.beanification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

/**
 * Simple dependency injection context for Fabric.
 * Scans the classpath for @Component classes, instantiates them,
 * injects @Autowired fields, and calls @PostConstruct methods.
 */
public class BeanContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanContext.class);

    private static final BeanContext INSTANCE = new BeanContext();

    private boolean initialized = false;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    /**
     * Initializes the bean context for the given mod ID.
     * Scans the "twilightforest" package for @Component classes,
     * instantiates them, injects dependencies, and calls @PostConstruct methods.
     */
    public static void init(String modId) {
        INSTANCE.doInit(modId);
    }

    /**
     * Returns a bean by type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T inject(Class<T> type) {
        return (T) INSTANCE.beans.get(type);
    }

    /**
     * Returns a bean by type and name.
     */
    @SuppressWarnings("unchecked")
    public static <T> T inject(Class<T> type, String name) {
        // Name-based lookup not fully implemented in simple version,
        // fall back to type-based lookup
        return inject(type);
    }

    /**
     * Injects @Autowired fields into an existing object.
     */
    public static void injectInto(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Autowired annotation = field.getAnnotation(Autowired.class);
                String name = annotation.value();
                Object dependency;
                if (name.isEmpty()) {
                    dependency = inject(field.getType());
                } else {
                    dependency = inject(field.getType(), name);
                }
                if (dependency != null) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, dependency);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Failed to inject @Autowired field: {} in {}", field.getName(), clazz.getName(), e);
                    }
                } else {
                    LOGGER.warn("Could not find bean for @Autowired field: {} of type {} in {}",
                            field.getName(), field.getType().getName(), clazz.getName());
                }
            } else if (field.isAnnotationPresent(InternalAutowired.class)) {
                InternalAutowired annotation = field.getAnnotation(InternalAutowired.class);
                String name = annotation.value();
                Object dependency;
                if (name.isEmpty()) {
                    dependency = inject(field.getType());
                } else {
                    dependency = inject(field.getType(), name);
                }
                if (dependency != null) {
                    field.setAccessible(true);
                    try {
                        field.set(obj, dependency);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("Failed to inject @InternalAutowired field: {} in {}", field.getName(), clazz.getName(), e);
                    }
                }
            }
        }
    }

    /**
     * Creates a lazy supplier for a bean by type.
     */
    public static <T> Supplier<T> injectLazy(Class<T> type) {
        return () -> inject(type);
    }

    /**
     * Returns true if the bean context has been initialized and frozen.
     */
    public boolean isFrozen() {
        return initialized;
    }

    private synchronized void doInit(String modId) {
        if (initialized) {
            LOGGER.info("BeanContext already initialized for mod {}", modId);
            return;
        }

        LOGGER.info("BeanContext initializing for mod {}", modId);

        // Scan for @Component classes in twilightforest package
        Set<Class<?>> componentClasses = ClassPathScanningUtil.findComponentClasses("twilightforest");
        LOGGER.info("Found {} @Component classes", componentClasses.size());

        // Instantiate all @Component classes
        List<Object> instances = new ArrayList<>();
        for (Class<?> clazz : componentClasses) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                instances.add(instance);
                beans.put(clazz, instance);
                LOGGER.debug("Instantiated bean: {}", clazz.getName());
            } catch (Throwable e) {
                // Catch Throwable (not just Exception) so NoClassDefFoundError,
                // ExceptionInInitializerError, VerifyError, LinkageError, etc.
                // never bubble up and crash the whole entrypoint.
                LOGGER.error("Failed to instantiate @Component class: {}", clazz.getName(), e);
            }
        }

        // Inject @Autowired fields into all beans
        for (Object bean : instances) {
            try {
                injectInto(bean);
            } catch (Throwable e) {
                LOGGER.error("Failed to inject @Autowired into bean: {}", bean.getClass().getName(), e);
            }
        }

        // Call @PostConstruct methods on all beans
        for (Object bean : instances) {
            try {
                callPostConstruct(bean);
            } catch (Throwable e) {
                LOGGER.error("Failed to call @PostConstruct on bean: {}", bean.getClass().getName(), e);
            }
        }

        // Also inject into the @Configurable TwilightForestMod class
        try {
            Class<?> twilightForestModClass = Class.forName("twilightforest.TwilightForestMod");
            injectInto(twilightForestModClass);
        } catch (Throwable e) {
            LOGGER.warn("Could not find TwilightForestMod class for injection", e);
        }

        initialized = true;
        LOGGER.info("BeanContext initialization complete for mod {}", modId);
    }

    private void callPostConstruct(Object bean) {
        Class<?> clazz = bean.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(bean);
                    LOGGER.debug("Called @PostConstruct method: {} on {}", method.getName(), clazz.getName());
                } catch (Exception e) {
                    LOGGER.error("Failed to call @PostConstruct method: {} on {}", method.getName(), clazz.getName(), e);
                }
            }
        }
    }
}
