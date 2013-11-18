/**
 * Copyright (c) 2013, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.controlsfx.samples;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.controlsfx.ControlsFXSample;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 *
 * Build the UI and launch the Application
 */
public class HelloSpreadsheetView extends ControlsFXSample {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleName() {
		return "SpreadsheetView";
	}

	@Override
	public Node getPanel(Stage stage) {
		BorderPane borderPane = new BorderPane();
		
		int rowCount = 50;
		int columnCount = 10;
		
		GridBase grid = new GridBase(rowCount, columnCount, generateRowHeight());
		normalGrid(grid);
		buildBothGrid(grid);

		SpreadsheetView spreadSheetView = new SpreadsheetView(grid);

		borderPane.setCenter(spreadSheetView);

		borderPane.setLeft(buildCommonControlGrid(spreadSheetView, borderPane,"Both"));
		
		return borderPane;
	}
	
	/**
	 * FIXME need to be removed afetr
	 * Compute RowHeight for test
	 * @return
	 */
	private Map<Integer,Double> generateRowHeight(){
		Map<Integer,Double> rowHeight = new HashMap<>();
		rowHeight.put(0, 50.0);
		rowHeight.put(5, 50.0);
		rowHeight.put(8, 70.0);
		rowHeight.put(12, 40.0);
		return rowHeight;
	}
	/**
	 * Build a common control Grid with some options on the left to control the
	 * SpreadsheetViewInternal
	 * @param gridType 
	 *
	 * @param spreadsheetView
	 * @return
	 */
	private GridPane buildCommonControlGrid(final SpreadsheetView spv,final BorderPane borderPane, String gridType) {
		final GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(5, 5, 5, 5));

		final CheckBox rowHeader = new CheckBox("Row Header");
		rowHeader.setSelected(true);
		rowHeader.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				spv.setShowRowHeader(arg2);
			}
		});

		final CheckBox columnHeader = new CheckBox("Column Header");
		columnHeader.setSelected(true);
		
		columnHeader.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(
					ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				spv.setShowColumnHeader(arg2);
			}
		});
		
		final CheckBox editable = new CheckBox("Editable");
		editable.setSelected(true);
		spv.editableProperty().bind(editable.selectedProperty());
		
		//In order to change the span style more easily
		final ChoiceBox<String> typeOfGrid = new ChoiceBox<String>(FXCollections.observableArrayList("Normal", "Both"));
		typeOfGrid.setValue(gridType);
		typeOfGrid.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number arg1, Number arg2) {
				if(arg2.equals(0)){
					int rowCount = 50;
					int columnCount = 10;
					GridBase grid = new GridBase(rowCount, columnCount, generateRowHeight());
					normalGrid(grid);
					
					SpreadsheetView spreadSheetView = new SpreadsheetView(grid);
					borderPane.setCenter(spreadSheetView);
					borderPane.setLeft(buildCommonControlGrid(spreadSheetView, borderPane,"Normal"));
				}else{
					int rowCount = 50;
					int columnCount = 10;
					GridBase grid = new GridBase(rowCount, columnCount, generateRowHeight());
					normalGrid(grid);
					buildBothGrid(grid);
					
					SpreadsheetView spreadSheetView = new SpreadsheetView(grid);
					borderPane.setCenter(spreadSheetView);
					borderPane.setLeft(buildCommonControlGrid(spreadSheetView, borderPane,"Both"));
				}
			}});
		
		grid.add(rowHeader, 1, 1);
		grid.add(columnHeader, 1, 2);
		grid.add(editable, 1, 3);
		grid.add(new Label("Span model:"), 1, 4);
		grid.add(typeOfGrid, 1, 5);

		return grid;
	}

	@Override
	public String getJavaDocURL() {
		return Utils.JAVADOC_BASE + "org/controlsfx/control/spreadsheet/SpreadsheetView.html";
	}

	private void normalGrid(GridBase grid) {
		ArrayList<ObservableList<SpreadsheetCell>> rows = new ArrayList<>(grid.getRowCount());
		for (int row = 0; row < grid.getRowCount(); ++row) {
			final ObservableList<SpreadsheetCell> dataRow = FXCollections.observableArrayList(); //new DataRow(row, grid.getColumnCount());
			for (int column = 0; column < grid.getColumnCount(); ++column) {
				dataRow.add(generateCell(row, column, 1, 1));
			}
			rows.add(dataRow);
		}
		grid.setRows(rows);
	}

	/**
	 * Randomly generate a dataCell(list or text)
	 *
	 * @param row
	 * @param column
	 * @param rowSpan
	 * @param colSpan
	 * @return
	 */
	private SpreadsheetCell generateCell(int row, int column, int rowSpan, int colSpan) {
		SpreadsheetCell cell;
		List<String> stringListTextCell = Arrays.asList("Shanghai","Paris","New York City","Bangkok","Singapore","Johannesburg","Berlin","Wellington","London","Montreal");
		final double random = Math.random();
		if (random < 0.10) {
			List<String> stringList = Arrays.asList("China","France","New Zealand","United States","Germany","Canada");
			cell = SpreadsheetCellType.LIST(stringList).createCell(row, column, rowSpan, colSpan, stringList.get((int)(Math.random()*6)));
		} else if (random >= 0.10 && random < 0.25) {
			cell = SpreadsheetCellType.STRING.createCell(row, column, rowSpan, colSpan,stringListTextCell.get((int)(Math.random()*10)));
		}else if (random >= 0.25 && random < 0.75) {
			cell = SpreadsheetCellType.DOUBLE.createCell(row, column, rowSpan, colSpan,(double)Math.round((Math.random()*100)*100)/100);
		}else{
			cell = SpreadsheetCellType.DATE.createCell(row, column, rowSpan, colSpan, LocalDate.now().plusDays((int)(Math.random()*10)));
		}

		// Styling for preview
		if(row%5 ==0){
			cell.getStyleClass().add("five_rows");
		}
		if(column == 0 && rowSpan == 1){
			cell.getStyleClass().add("row_header");
		}
		if(row == 0) {
			cell.getStyleClass().add("col_header");
		}
		return cell;
	}
	
	/**
	 * Build a sample RowSpan and ColSpan grid
	 * @param grid
	 */
	private void buildBothGrid(GridBase grid) {
		grid.spanRow(2, 2, 2);
		grid.spanColumn(2, 2, 2);

		grid.spanRow(4, 2, 4);

		grid.spanColumn(5, 8, 2);

		grid.spanRow(15, 3, 8);

		grid.spanRow(3, 5, 5);
		grid.spanColumn(3, 5, 5);

		grid.spanRow(2, 10, 4);
		grid.spanColumn(3, 10, 4);

		grid.spanRow(2, 12, 3);
		grid.spanColumn(3, 22, 3);

		grid.spanRow(1, 27, 4);

		grid.spanColumn(4, 30, 3);
		grid.spanRow(4, 30, 3);
	}
}
