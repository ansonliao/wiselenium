package org.wiselenium.core.pagefactory;

import java.lang.reflect.Field;

import net.sf.cglib.proxy.Enhancer;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

/**
 * Utility class to create Pages.
 * 
 * @author Andre Ricardo Schaffer
 * @since 0.0.1
 */
public final class WisePageFactory {
	
	private WisePageFactory() {}
	
	/**
	 * As {@link #initElements(WebDriver, Class)} but will only replace the element fields of an
	 * already instantiated Page Object.
	 * 
	 * @param <T> The type of the instance.
	 * @param searchContext The context that will be used to look up the elements.
	 * @param instance The instance whose fields should be proxied.
	 * @return The instance with its element fields proxied.
	 * @since 0.0.1
	 */
	public static <T> T initElements(SearchContext searchContext, T instance) {
		WiseDecorator decorator = new WiseDecorator(new DefaultElementLocatorFactory(searchContext));
		return initElements(decorator, instance);
	}
	
	/**
	 * Instantiates an instance of the given class, and set a lazy proxy for each of its element
	 * fields (WebElement, List&ltWebElement&gt or any type annotated with Field, Container or
	 * Frame). <br/>
	 * It assumes the element field name as the HTML element's "id" or "name". To change how the
	 * element is located, use the FindBy annotation. <br/>
	 * This method will attempt to instantiate the class given to it, using either a constructor
	 * which takes a WebDriver instance as its only argument or falling back on a no-arg
	 * constructor. An exception will be thrown if the class cannot be instantiated. <br/>
	 * In case the no-arg constructor is used, the page itself is proxied to wrap the WebDriver,
	 * which can be unwrapped using the WiseUnrwapper.
	 * 
	 * @param <T> The class type that will be initialized.
	 * @param driver The driver that will be used to look up the elements of the object.
	 * @param clazz The class to be initialized.
	 * @return An instance of the class with its element fields proxied.
	 * @since 0.0.1
	 */
	public static <T> T initElements(WebDriver driver, Class<T> clazz) {
		T instance = instantiatePage(driver, clazz);
		return initElements(driver, instance);
	}
	
	private static <T> T initElements(FieldDecorator decorator, T instance) {
		Class<?> currentInstanceHierarchyClass = instance.getClass();
		while (currentInstanceHierarchyClass != Object.class) {
			proxyElements(decorator, instance, currentInstanceHierarchyClass);
			currentInstanceHierarchyClass = currentInstanceHierarchyClass.getSuperclass();
		}
		
		return instance;
	}
	
	private static <T> T instantiatePage(WebDriver driver, Class<T> clazz) {
		T instance;
		try {
			instance = instantiatePageWithWebDriverConstructor(driver, clazz);
		} catch (ClassWithoutConstructorThatTakesWebDriverException e) {
			instance = instantiatePageWithEmptyConstructor(driver, clazz);
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T instantiatePageWithEmptyConstructor(WebDriver driver, Class<T> clazz) {
		Enhancer e = new Enhancer();
		e.setSuperclass(clazz);
		e.setInterfaces(new Class[] { WrapsDriver.class });
		e.setCallback(WisePageProxy.getInstance(driver));
		return (T) e.create();
	}
	
	private static <T> T instantiatePageWithWebDriverConstructor(WebDriver driver, Class<T> clazz) {
		try {
			return clazz.getConstructor(WebDriver.class).newInstance(driver);
		} catch (Exception e) {
			throw new ClassWithoutConstructorThatTakesWebDriverException(clazz, e);
		}
	}
	
	private static void proxyElements(FieldDecorator decorator, Object instance,
		Class<?> instanceHierarchyClass) {
		
		Field[] fields = instanceHierarchyClass.getDeclaredFields();
		for (Field field : fields) {
			Object value = decorator.decorate(instance.getClass().getClassLoader(), field);
			if (value != null) try {
				field.setAccessible(true);
				field.set(instance, value);
			} catch (IllegalAccessException e) {
				throw new PageCreationException(instance.getClass(), e);
			}
		}
	}
	
}
