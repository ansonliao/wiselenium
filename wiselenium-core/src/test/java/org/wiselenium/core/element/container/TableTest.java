package org.wiselenium.core.element.container;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.wiselenium.core.element.container.TablePage.COL_SPAN;
import static org.wiselenium.core.element.container.TablePage.ROW_SPAN;
import static org.wiselenium.core.element.container.TablePage.TABLE_BODY_CELL1;
import static org.wiselenium.core.element.container.TablePage.TABLE_BODY_CELL2;
import static org.wiselenium.core.element.container.TablePage.TABLE_CAPTION;
import static org.wiselenium.core.element.container.TablePage.TABLE_HEAD_CELL1;
import static org.wiselenium.core.element.container.TablePage.TABLE_HEAD_CELL2;
import static org.wiselenium.core.pagefactory.WisePageFactory.initElements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wiselenium.core.test.WiseTestNG;

@SuppressWarnings("javadoc")
public class TableTest extends WiseTestNG<TableTest> {
	
	TablePage page;
	
	
	@BeforeMethod
	public void initPage() {
		this.page = initElements(this.getDriver(), TablePage.class).and().get();
	}
	
	@Test
	public void shouldFindElementsInsideCells() {
		Table table = this.page.getComplexTableWithFoot();
		TableBody tbody = table.getBody();
		
		TableRow firstRow = tbody.getRow(0);
		TableCell firstRowSecondCell = firstRow.getCell(1);
		Table innerTable = firstRowSecondCell.findElement(Table.class, By.tagName("table"));
		assertNotNull(innerTable);
		assertFalse(innerTable.getBody().getRows().isEmpty());
		
		TableRow secondRow = tbody.getRow(1);
		TableCell secondRowFirstCell = secondRow.getCell(0);
		WebElement ul = secondRowFirstCell.findElement(WebElement.class, By.tagName("ul"));
		assertNotNull(ul);
	}
	
	@Test
	public void shouldGetBody() {
		Table tableWithCaptionAndHead = this.page.getTableWithCaptionAndHead();
		TableBody body = tableWithCaptionAndHead.getBody();
		assertNotNull(body);
		
		List<TableRow> rows = body.getRows();
		assertFalse(rows.isEmpty());
		
		String[] cellsTexts = { TABLE_BODY_CELL1, TABLE_BODY_CELL2 };
		
		TableRow row = rows.get(0);
		List<TableCell> cells = row.getCells();
		assertFalse(cells.isEmpty());
		for (int i = 0; i < cells.size(); i++) {
			TableCell tableCell = cells.get(i);
			String cellText = cellsTexts[i];
			assertEquals(tableCell.getText(), cellText);
		}
		
		assertNotNull(this.page.getTableWithColspan().getBody());
	}
	
	@Test
	public void shouldGetCaption() {
		Table tableWithCaptionAndHead = this.page.getTableWithCaptionAndHead();
		assertEquals(tableWithCaptionAndHead.getCaption(), TABLE_CAPTION);
		
		assertNull(this.page.getTableWithColspan().getCaption());
	}
	
	@Test
	public void shouldGetColspan() {
		Table tableWithColspan = this.page.getTableWithColspan();
		int cellWithColspan = tableWithColspan.getBody().getRows().get(0).getCells().get(1)
			.getColSpan();
		assertEquals(cellWithColspan, COL_SPAN);
		
		Table tableWithCaptionAndHead = this.page.getTableWithCaptionAndHead();
		int cellWithoutColspan = tableWithCaptionAndHead.getBody().getRows().get(0).getCells()
			.get(0).getColSpan();
		assertEquals(cellWithoutColspan, 1);
	}
	
	@Test
	public void shouldGetFoot() {
		Table complexTableWithFoot = this.page.getComplexTableWithFoot();
		TableFoot foot = complexTableWithFoot.getFoot();
		assertNotNull(foot);
		
		List<TableRow> rows = foot.getRows();
		assertFalse(rows.isEmpty());
		
		String[] cellsTexts = { TablePage.TABLE_FOOT_CELL1, TablePage.TABLE_FOOT_CELL2 };
		
		TableRow row = foot.getRow(0);
		List<TableCell> cells = row.getCells();
		assertFalse(cells.isEmpty());
		for (int i = 0; i < cells.size(); i++) {
			TableCell tableCell = cells.get(i);
			String cellText = cellsTexts[i];
			assertEquals(tableCell.getText(), cellText);
		}
		
		assertNull(this.page.getTableWithColspan().getFoot());
	}
	
	@Test
	public void shouldGetHead() {
		Table tableWithCaptionAndHead = this.page.getTableWithCaptionAndHead();
		TableHead head = tableWithCaptionAndHead.getHead();
		assertNotNull(head);
		
		List<TableRow> rows = head.getRows();
		assertFalse(rows.isEmpty());
		
		String[] cellsTexts = { TABLE_HEAD_CELL1, TABLE_HEAD_CELL2 };
		
		TableRow row = head.getRow(0);
		List<TableCell> cells = row.getCells();
		assertFalse(cells.isEmpty());
		for (int i = 0; i < cells.size(); i++) {
			TableCell tableCell = cells.get(i);
			String cellText = cellsTexts[i];
			assertEquals(tableCell.getText(), cellText);
		}
		
		assertNull(this.page.getTableWithColspan().getHead());
	}
	
	@Test
	public void shouldGetOnlyDirectChildRows() {
		Table table = this.page.getComplexTableWithFoot();
		TableBody tbody = table.getBody();
		
		assertEquals(tbody.getRows().size(), 2);
		assertEquals(tbody.getRow(0).getCells().size(), 2);
		assertEquals(tbody.getRow(1).getCells().size(), 2);
		
		Table innerTable = tbody.getRow(0).getCell(1).findElement(Table.class, By.tagName("table"));
		TableBody innerTableBody = innerTable.getBody();
		assertEquals(innerTableBody.getRows().size(), 2);
		assertEquals(innerTableBody.getRow(0).getCells().size(), 2);
		assertEquals(innerTableBody.getRow(1).getCells().size(), 2);
	}
	
	@Test
	public void shouldGetRowspan() {
		Table tableWithRowspan = this.page.getTableWithRowspan();
		int cellWithRowspan = tableWithRowspan.getBody().getRows().get(1).getCells().get(0)
			.getRowSpan();
		assertEquals(cellWithRowspan, ROW_SPAN);
		
		Table tableWithCaptionAndHead = this.page.getTableWithCaptionAndHead();
		int cellWithoutRowspan = tableWithCaptionAndHead.getBody().getRows().get(0).getCells()
			.get(0).getRowSpan();
		assertEquals(cellWithoutRowspan, 1);
	}
	
}
