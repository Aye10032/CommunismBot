package com.aye10032.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Modifier;
import java.util.*;

@Component
@Order(1)
@Slf4j
public class SpringUtils implements BeanDefinitionRegistryPostProcessor {

    public static BeanDefinitionRegistry registry;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        SpringUtils.registry = registry;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 通过扫描类的包路径下的指定子类向系统中注册 bean <br>
     * 扫描到的类会走一遍bean的生命周期，其中的依赖也会自动注入
     *
     * @param packagePath 需要扫描类的包路径
     * @return 自动生成的bean名称
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> Map<Class<? extends T>, String> registerBeansByParentClass(Class<T> parentClass, String packagePath) throws BeanDefinitionStoreException {
        Set<Class<? extends T>> subClasses = ReflectionUtils.getSubClass(parentClass, packagePath);
        if (CollectionUtils.isEmpty(subClasses)) {
            return Collections.emptyMap();
        }
        Map<Class<? extends T>, String> result = new HashMap<>(subClasses.size());
        //获取该包下所有parentClass的子类，初始化bean并注册
        for (Class<? extends T> beanClazz : subClasses) {
            result.put(beanClazz, registerBean(beanClazz));
        }
        return result;
    }

    /**
     * 主动向Spring容器中注册bean
     *
     * @param registry  Bean定义注册表
     * @param beanName  BeanName
     * @param aliases   别名
     * @param beanClazz 注册的bean的类性
     * @param args      构造方法的必要参数，顺序和类型要求和clazz中定义的一致
     */
    public static void registerBean(BeanDefinitionRegistry registry, String beanName, @Nullable String[] aliases, Class<?> beanClazz,
                                    Object... args) throws BeanDefinitionStoreException {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                builder.addConstructorArgValue(arg);
            }
        }
        registerBean(registry, new BeanDefinitionHolder(builder.getRawBeanDefinition(), beanName, aliases));
    }


    /**
     * 通过自动生成的bean名称注册bean，会自动注入依赖
     *
     * @param beanClazz 要生成的beanClass
     * @return 返回生成的Bean名称 {@link BeanDefinitionReaderUtils#generateBeanName}
     */
    public static String registerBean(Class<?> beanClazz) throws BeanDefinitionStoreException {
        return BeanDefinitionReaderUtils.registerWithGeneratedName(getAutowireBeanDefinition(beanClazz), registry);
    }

    /**
     * 注册bean，会自动注入依赖
     *
     * @param beanClazz 要生成的beanClass
     * @param beanName  Bean名称
     * @param aliases   别名
     */
    public static void registerBean(Class<?> beanClazz, String beanName, String... aliases) throws BeanDefinitionStoreException {
        registerBean(registry, beanClazz, beanName, aliases);
    }

    /**
     * 注册bean，会自动注入依赖
     *
     * @param registry  Bean定义注册表
     * @param beanClazz 要生成的beanClass
     * @param beanName  Bean名称
     * @param aliases   别名
     */
    public static void registerBean(BeanDefinitionRegistry registry, Class<?> beanClazz, String beanName, String... aliases) throws BeanDefinitionStoreException {
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(getAutowireBeanDefinition(beanClazz), beanName, aliases);
        registerBean(registry, beanDefinitionHolder);
    }

    /**
     * 注册bean，会自动注入依赖
     *
     * @param registry         Bean定义注册表
     * @param definitionHolder 带有名称和别名的Bean定义的持有者
     */
    public static void registerBean(BeanDefinitionRegistry registry, BeanDefinitionHolder definitionHolder) throws BeanDefinitionStoreException {
        validateBeanName(definitionHolder);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    /**
     * 获取bean定义，已经设置好按照类型自动注入依赖
     */
    private static GenericBeanDefinition getAutowireBeanDefinition(Class<?> beanClazz) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
        builder.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        //org.springframework.beans.factory.support.DefaultListableBeanFactory.registerBeanDefinition
        builder.setRole(BeanDefinition.ROLE_APPLICATION);
        return (GenericBeanDefinition) builder.getRawBeanDefinition();
    }

    private static void validateBeanName(BeanDefinitionHolder definitionHolder) {
        String beanName = definitionHolder.getBeanName();
        if (registry.isBeanNameInUse(beanName)) {
            if (log.isDebugEnabled()) {
                if (registry.isAlias(beanName)) {
                    log.debug("Overriding bean alias with a different definition: replacing One bean alias with [" + definitionHolder.getBeanDefinition() + "]");
                } else {
                    log.debug("Overriding bean definition for bean '" + beanName +
                        "' with a different definition: replacing [" + registry.getBeanDefinition(beanName) +
                        "] with [" + definitionHolder.getBeanDefinition() + "]");
                }

            } else {
                log.info("Overriding bean definition for bean '" + beanName + "'");
            }
        }
        if (definitionHolder.getAliases() != null) {
            List<String> usedAliases = new ArrayList<>(definitionHolder.getAliases().length);
            for (String alias : definitionHolder.getAliases()) {
                if (registry.isAlias(alias)) {
                    usedAliases.add(alias);
                }
            }
            if (!usedAliases.isEmpty()) {
                if (log.isDebugEnabled()) {
                    log.debug("Overriding bean alias with a different definition: replacing One bean alias " + Arrays.toString(usedAliases.toArray()) + "with [" + definitionHolder.getBeanDefinition() + "]");
                }
            }
        }
    }

    /**
     * 通过父类class和类路径获取该路径下父类的所有子类列表
     *
     * @param parentClass 父类或接口的class
     * @param packagePath 类路径
     * @return 所有该类子类或实现类的列表
     */
    @SneakyThrows(ClassNotFoundException.class)
    public static <T> List<Class<T>> getSubClasses(final Class<T> parentClass, final String packagePath) {
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(parentClass));
        final Set<BeanDefinition> components = provider.findCandidateComponents(packagePath);
        final List<Class<T>> subClasses = new ArrayList<>();
        for (final BeanDefinition component : components) {
            @SuppressWarnings("unchecked") final Class<T> cls = (Class<T>) Class.forName(component.getBeanClassName());
            if (Modifier.isAbstract(cls.getModifiers())) {
                continue;
            }
            subClasses.add(cls);
        }
        return subClasses;
    }
}
