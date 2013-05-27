package org.wiselenium.core.element.container;

import org.wiselenium.core.WiseQuery;
import org.wiselenium.core.element.Element;

/**
 * Represents an abstraction of a HTML Container.
 * 
 * @author Andre Ricardo Schaffer
 * @param <T> The container type.
 * @since 0.0.1
 */
@org.wiselenium.core.Container
public interface Container<T extends Container<T>> extends Element<T>, WiseQuery {
	
}
