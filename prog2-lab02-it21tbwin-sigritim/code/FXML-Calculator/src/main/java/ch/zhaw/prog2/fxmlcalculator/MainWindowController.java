package ch.zhaw.prog2.fxmlcalculator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * Controller for the MainWindow. One controller per mask (or FXML file)
 * Contains everything the controller has to reach in the view (controls)
 * and all methods the view calls based on events.
 * @author
 * @version 1.0
 */
public class MainWindowController{

    private ValueHandler valueHandler;

    private final String HELPTEXT = "Enter valid values to " +System.lineSeparator()+
        "-Initial amount (>0)" +System.lineSeparator()+
        "-Return in % (can be +/- or 0)"+System.lineSeparator()+
        "-Annual Costs (>0)"+System.lineSeparator()+
        "-Number of years (>0)" +System.lineSeparator() + System.lineSeparator() +
        "Calculate displays the annual balance development!";

    private boolean[] toogleMenuItems = {false, false, false, false};

    @FXML private CheckMenuItem menuItemIInitialAmount;
    @FXML private CheckMenuItem menuItemReturInProcent;
    @FXML private CheckMenuItem menuItemAnnualCost;
    @FXML private CheckMenuItem menuItemNumberOfYears;
    @FXML private MenuItem menuItemClearValue;
    @FXML private MenuItem menuItemClearResults;
    @FXML private MenuItem menuItemIShowHelp;

    @FXML private TextField textfieldInitialAmount;
    @FXML private TextField textfieldReturnRate;
    @FXML private TextField textfieldAnnualCost;
    @FXML private TextField textfieldNumberOfYears;
    @FXML private TextArea textAreaResult;

    @FXML private Button buttonCalculate;
    @FXML private Button buttonClose;
    @FXML private Button OpenResWindow;


    @FXML
    private void closeWindow() {
        Stage mainStage = (Stage) buttonClose.getScene().getWindow();
        mainStage.close();
    }

    @FXML
    private void calculate() {
        valueHandler.checkValuesAndCalculateResult(textfieldInitialAmount.getText(), textfieldReturnRate.getText(), textfieldAnnualCost.getText(), textfieldNumberOfYears.getText());
        if (valueHandler.areValuesOk()) {
            textAreaResult.setStyle("-fx-border-color: green");
        } else {
            textAreaResult.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    private void openResultWindow () {
        try {
            FXMLLoader loaderResultWindow = new FXMLLoader(getClass().getResource("ResultWindow.fxml"));
            Parent resultRoot = loaderResultWindow.load();
            ResultWindowController resultWindowController = loaderResultWindow.getController();
            resultWindowController.bindResults(valueHandler.resultBoundProperty());
            Stage stage = new Stage();;
            stage.setTitle("Resultwindow");
            stage.setScene(new Scene(resultRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void toogleInitialAmount() {
        toogleMenuItems[0] = !toogleMenuItems[0];
        menuItemIInitialAmount.setSelected(toogleMenuItems[0]);
    }

    @FXML
    public void toogleReturnInProcent() {
        toogleMenuItems[1] = !toogleMenuItems[1];
        menuItemReturInProcent.setSelected(toogleMenuItems[1]);
    }

    @FXML
    public void toogleAnnualCost() {
        toogleMenuItems[2] = !toogleMenuItems[2];
        menuItemAnnualCost.setSelected(toogleMenuItems[2]);
    }

    @FXML
    public void toogleNumberOfYears() {
        toogleMenuItems[3] = !toogleMenuItems[3];
        menuItemNumberOfYears.setSelected(toogleMenuItems[3]);
    }

    @FXML
    public void clearValues() {
        if (menuItemIInitialAmount.isSelected()) textfieldInitialAmount.setText("");
        if (menuItemReturInProcent.isSelected()) textfieldReturnRate.setText("");
        if (menuItemAnnualCost.isSelected()) textfieldAnnualCost.setText("");
        if (menuItemNumberOfYears.isSelected()) textfieldNumberOfYears.setText("");
    }

    @FXML
    public void clearAllValues() {
        valueHandler.clearResult();
    }

    @FXML
    public void showHelp() {
        valueHandler.setResultBound(HELPTEXT);
        textAreaResult.setStyle("-fx-border-color: blue");
    }

    public void setValueHandler(ValueHandler valueHandler) {
        this.valueHandler = valueHandler;
        this.valueHandler.addListener(new IsObserver() {
            @Override
            public void update() {
                textAreaResult.setText(valueHandler.getResultBound());
            }
        });
    }



}
