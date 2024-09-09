package ch.zhaw.prog2.calculator;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Main Application. Controller and View in the same class
 * Other options:
 * - Separating the start from the window classes
 * - Separating view and Controller
 * @author bles
 *
 */
public class Main extends Application {
	private static final int VERTICAL_GAP = 5;
	private static final int HORIZONTAL_GAP = 10;

    private static final String INFO = """
        Enter valid values to
        - Initial amount (> 0)
        - Return in % (can be +/- or 0)
        - Annual Costs (> 0)
        - Number of years (> 0)
        Calculate displays the annual balance development!";
        """;

	private Stage primaryStage;
	private CheckMenuItem clearInitialAmount = new CheckMenuItem("Initial amount");
	private CheckMenuItem clearReturnInPercent = new CheckMenuItem("Return in %");
	private CheckMenuItem clearAnnualCosts = new CheckMenuItem("Annual Costs");
	private CheckMenuItem clearNumberOfYears = new CheckMenuItem("Number of years");
	private TextField initialAmount = new TextField();
	private TextField returnInPercent = new TextField();
	private TextField annualCost = new TextField();
	private TextField numberOfYears = new TextField();
	private TextArea results = new TextArea();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		mainWindow();
	}

	/*
	 * Create the window, call methods to create the different parts of the
	 * scene graph. Put the parts together in appropriate panes.
	 */
	private void mainWindow() {
		try {
			BorderPane rootPane = new BorderPane();
			// BorderPane top
	        MenuBar menuBar = new MenuBar();
	        createMenu(menuBar);
	        // BorderPane left
	        // two rows for grid (inputPanel) and other VBox (resultRows)
			VBox inputOutputPanel = new VBox(VERTICAL_GAP);
			GridPane inputPanel = new GridPane();
			VBox resultRows = new VBox();
			inputOutputPanel.getChildren().add(inputPanel);
			inputOutputPanel.getChildren().add(resultRows);
			createInputPanel(inputPanel);
			createResultRows(resultRows);
 			// BorderPane bottom
			HBox buttons = new HBox(HORIZONTAL_GAP);
			buttons.setAlignment(Pos.BASELINE_CENTER);
			createButtons(buttons);

			// set up root border pane
			rootPane.setTop(menuBar);
			rootPane.setBottom(buttons);
			rootPane.setCenter(inputOutputPanel);

			// Create scene with root node with size
			Scene scene = new Scene(rootPane, 600, 400);
			primaryStage.setMinWidth(280);
			// Set stage properties
			primaryStage.setTitle("Return on Investment Calculator");
			// Add scene to the stage and make it visible
			primaryStage.setScene(scene);
			primaryStage.show();

			// Connect height of the result-area to the height of the scene
			scene.heightProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					results.setPrefHeight((double) newValue);
				}
			});

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Add the title and the result TextArea to the VBox
	 */
	private void createResultRows(VBox resultRows) {
		resultRows.getChildren().add(new Label("Results:"));
		resultRows.getChildren().add(results);
		resultRows.setPadding(new Insets(VERTICAL_GAP, HORIZONTAL_GAP, VERTICAL_GAP, HORIZONTAL_GAP));
	}

	/*
	 * 4 rows in a GridPane with row-title and input TextField
	 */
	private void createInputPanel(GridPane inputPanel) {
		inputPanel.setVgap(5);
		inputPanel.setHgap(5);
		inputPanel.add(new Label("Initial amount"), 0, 1);
		inputPanel.add(new Label("Return rate in %"), 0, 2);
		inputPanel.add(new Label("Annual cost"), 0, 3);
		inputPanel.add(new Label("Number of years"), 0, 4);
		inputPanel.add(initialAmount, 1, 1);
		inputPanel.add(returnInPercent, 1, 2);
		inputPanel.add(annualCost, 1, 3);
		inputPanel.add(numberOfYears, 1, 4);
		inputPanel.setPadding(new Insets(VERTICAL_GAP, HORIZONTAL_GAP, VERTICAL_GAP, HORIZONTAL_GAP));
	}

	/*
	 * Create menu for the top-area of the BorderPane
	 */
	private void createMenu(MenuBar menu) {
        Menu clearMenu = new Menu("Clear");
        Menu helpMenu = new Menu("?");

        // Create MenuItems
        MenuItem clearValues = new MenuItem("Clear values");
        clearValues.setId("clearValues");
        MenuItem clearResults = new MenuItem("Clear results");
        clearResults.setId("clearResults");
        MenuItem helpShowText = new MenuItem("Show help");

		clearMenu.getItems().addAll(clearInitialAmount, clearReturnInPercent, clearAnnualCosts, clearNumberOfYears);
		clearMenu.getItems().addAll(new SeparatorMenuItem(), clearValues, new SeparatorMenuItem(), clearResults);
		helpMenu.getItems().add(helpShowText);

		menu.getMenus().addAll(clearMenu, helpMenu);
		//using an inner class
		ClearHandler clearHandler = new ClearHandler();
		clearValues.addEventHandler(ActionEvent.ACTION, clearHandler);
		clearResults.addEventHandler(ActionEvent.ACTION, clearHandler);

		helpShowText.setAccelerator(KeyCombination.keyCombination("F1"));
		helpShowText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showResult(INFO, true, Color.BLUE);
			}
		});
	}

	/*
	 * Create buttons in the HBox inside the bottom pane of the BorderPane
	 */
	private void createButtons(HBox buttons) {
		Button closeButton = new Button("Close");
		Button calculateButton = new Button("Calculate");
		// Configure close event using lambda expressions
		closeButton.setOnAction(e -> Platform.exit());
		// Configure calculate event using anonymous inner class
		calculateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ValueHandler valueHandler = new ValueHandler();
				valueHandler.checkValuesAndCalculateResult(
				    initialAmount.getText(),
                    returnInPercent.getText(),
                    annualCost.getText(),
                    numberOfYears.getText());
				String result = valueHandler.getResultBound();
				if(valueHandler.areValuesOk()) {
					showResult(result, true, Color.GREEN);
				} else {
					showResult(result, false, Color.RED);
				}
			}
		});
		buttons.getChildren().addAll(calculateButton, closeButton);
		buttons.setPadding(new Insets(VERTICAL_GAP, HORIZONTAL_GAP, VERTICAL_GAP, HORIZONTAL_GAP));
	}

	/*
	 * Show text in the result box
	 */
	private void showResult(String text, boolean clearFirst, Color backColor) {
		if(clearFirst) {
			results.setText(text);
		} else {
			results.appendText("\n");
			results.appendText(text);
		}
		results.setBorder(new Border(new BorderStroke(backColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
	}

	/*
	 * Handler to clear the controls
	 */
	private class ClearHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
            switch (((MenuItem) event.getSource()).getId()) {
                case "clearValues" -> {
                    if (clearInitialAmount.isSelected()) {
                        initialAmount.clear();
                    }
                    if (clearAnnualCosts.isSelected()) {
                        annualCost.clear();
                    }
                    if (clearNumberOfYears.isSelected()) {
                        numberOfYears.clear();
                    }
                    if (clearReturnInPercent.isSelected()) {
                        returnInPercent.clear();
                    }
                }
                case "clearResults" -> results.clear();
            }
		}
	}

}

