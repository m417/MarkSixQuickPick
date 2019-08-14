package controller;

import java.util.ArrayList;
import java.util.List;

import model.MSQPmodel;
import view.MSQPview;

public class MSQPcontroller {

	private MSQPmodel myModel;
	private MSQPview myView;

	public MSQPcontroller(MSQPmodel theModel, MSQPview theView) {
		myModel = theModel;
		myView = theView;

		addActionListener();
	}

	private void addActionListener() {
		myView.addGenerateActionListener(theEvent -> generateCombinations());
	}

	private void generateCombinations() {
		int totalSelections = myView.getTotalSelections();
		int numOfEntries = myView.getNumOfEntries();
		char investmentType = myView.getInvestmentType();

		List<List<Integer>> dataList = new ArrayList<>();

		for (int i = 0; i < numOfEntries; i++) {
			dataList.add(myModel.getCombination(totalSelections));
		}

		String[][] dataArray = new String[dataList.size()][2];
		for (int i = 0; i < dataList.size(); i++) {
			dataArray[i][0] = "[" + String.valueOf(i + 1) + "]";
			dataArray[i][1] = dataList.get(i).toString();
		}

		myView.updateData(dataArray, myModel.getTotalInvestment(totalSelections, investmentType));
	}

	public MSQPmodel getModel() {
		return myModel;
	}

	public MSQPview getView() {
		return myView;
	}

	public void showView() {
		myView.createAndShowGUI();
	}

}
