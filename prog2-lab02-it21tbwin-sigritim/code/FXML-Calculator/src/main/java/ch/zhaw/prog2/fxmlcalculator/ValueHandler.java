package ch.zhaw.prog2.fxmlcalculator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Handles the values from the input form.
 * Offers the {@link #resultBound} StringProperty to listen from a view (bind to a field in the view or add a listener)
 * @author bles
 * @version 1.1
 */
public class ValueHandler implements IsObservable {

	private double initialAmount;
	private double returnInPercent;
	private double annualCost;
	private int numberOfYears;
	private boolean valuesOk = false;
	// Solution with bound properties
	private StringProperty resultBound = new SimpleStringProperty();

    private List<IsObserver> listener = new ArrayList<>();

	/**
	 * Check the input values are valid (can be improved)
	 * If not ok, return an error message
	 * If ok, set the data fields and return an empty string
     * @return empty string on success or error message on invalid value
	 */
	private String checkAndSetValues(String initialAmount, String returnInPercent, String annualCost, String numberOfYears) {
		StringBuilder sb = new StringBuilder();
		valuesOk = true;
		if ("".equals(initialAmount) || Double.parseDouble(initialAmount)<=0) {
			sb.append("Please specify a positive  initial amount!\n");
			valuesOk = false;
		} else {
			this.initialAmount = Double.parseDouble(initialAmount);
		}
		if ("".equals(returnInPercent)) {
			sb.append("Please specify the annual return rate in %!\n");
			valuesOk = false;
		} else {
			this.returnInPercent = Double.parseDouble(returnInPercent)/100;
		}
		if ("".equals(annualCost) || Double.parseDouble(annualCost)<0) {
			sb.append("Please specify the annual cost!\n");
			valuesOk = false;
		} else {
			this.annualCost = Double.parseDouble(annualCost);
		}
		if ("".equals(numberOfYears) ||
				Double.parseDouble(numberOfYears) < 1 ||
				Double.parseDouble(numberOfYears) > 99 ||
				Math.round(Double.parseDouble(numberOfYears))!=Double.parseDouble(numberOfYears)) {
			sb.append("Please enter a time period in years!");
			valuesOk = false;
		} else {
			this.numberOfYears = Integer.parseInt(numberOfYears);
		}
		return sb.toString();
	}

    /**
     * If the values checked by {@link #checkAndSetValues(String, String, String, String)} are ok, the return is true
     * @return	true, if values are ok
     */
    public boolean areValuesOk() {
        return valuesOk;
    }

    /**
	 * Calculates the result
	 * @return the result as a String
	 */
	private String calculateResult() {
		StringBuilder resultSB = new StringBuilder();
		double val = initialAmount;
		for(int i = 1; i <= numberOfYears; i++) {
			resultSB.append("After ");
			resultSB.append(i).append(" year(s): ");
			val = val * (1 + returnInPercent) - annualCost;
			resultSB.append(Math.round(val)).append("\n");
		}
		return resultSB.toString();
	}


    // Solution with bound properties
    /**
     * String containing the result of the calculation
     * Can be "", if no calculation or check is done or could contain the error message on invalid values
	 * @return	String with the result of the value checking or the calculation
	 */
	public String getResultBound() {
		return resultBound.get();
	}

	/**
	 * Sets the result of the calculation (or error message).
	 * @param infoText
	 */
	public void setResultBound(String infoText) {
		resultBound.set(infoText);
        for(IsObserver observer : listener) {
            observer.update();
        }
	}


	/**
	 * Gives access to the StringProperty holding the result of the calculation
	 * @return result String property which can be bound to UI elements
	 */
	public StringProperty resultBoundProperty() {
		return resultBound;
	}

    /**
     * Checks the values and calculates the result. All values as String (from the Text-Fields)
     * If the check fails, an error message is set to the bound result property
     * @param initialAmount
     * @param returnInPercent
     * @param annualCost
     * @param numberOfYears
     */
    public void checkValuesAndCalculateResult(String initialAmount, String returnInPercent, String annualCost, String numberOfYears) {
        setResultBound(checkAndSetValues(initialAmount, returnInPercent, annualCost, numberOfYears));
        if(valuesOk) {
            setResultBound(calculateResult());
        }
    }

    /**
     * clears result and resets all values
     */
	public void clearResult() {
	    initialAmount = 0;
	    returnInPercent = 0;
	    annualCost = 0;
	    numberOfYears = 0;
	    setResultBound("");
	    valuesOk = false;
    }


    @Override
    public void addListener(IsObserver observer) {
        listener.add(observer);
    }

    @Override
    public void removeListener(IsObserver observer) {
        listener.remove(observer);
    }
}
