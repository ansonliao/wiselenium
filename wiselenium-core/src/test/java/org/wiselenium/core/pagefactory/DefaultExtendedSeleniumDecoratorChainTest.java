package org.wiselenium.core.pagefactory;

import static org.wiselenium.core.pagefactory.WisePageFactory.initElements;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wiselenium.core.element.field.Text;
import org.wiselenium.core.pagefactory.dummy.DummyPage;
import org.wiselenium.core.test.WiseTestNG;

@SuppressWarnings("javadoc")
public class DefaultExtendedSeleniumDecoratorChainTest extends
	WiseTestNG<DefaultExtendedSeleniumDecoratorChainTest> {
	
	private DummyPage page;
	private DefaultExtendedSeleniumDecoratorChain decorator;
	
	
	@BeforeClass
	public void initDecorator() {
		this.page = initElements(this.getDriver(), DummyPage.class);
		this.decorator = new DefaultExtendedSeleniumDecoratorChain(
			new DefaultElementLocatorFactory(this.getDriver()));
	}
	
	@Test
	public void shouldDecorate() {
		WebElement element = this.decorator.decorate(WebElement.class, this.page.getText());
		Assert.assertNotNull(element);
	}
	
	@Test
	public void shouldReturnEmptyListOnDecorateElementsWhenItShouldntDecorateAndTheresNoNextDecorator() {
		List<Text> list = this.decorator.decorate(Text.class, this.page.getRadiobuttons());
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void shouldReturnNullOnDecorateElementWhenItShouldntDecorateAndTheresNoNextDecorator() {
		Text element = this.decorator.decorate(Text.class, this.page.getText());
		Assert.assertNull(element);
	}
	
}
