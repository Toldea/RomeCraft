package toldea.romecraft.entity.ai.fsm;

import java.util.HashMap;

public abstract class StateMachine {
	private HashMap<String, Object> variableMap = null;
	
	protected State activeState = null;

	public abstract void initialize();

	public void setVariable(String key, Object variable) {
		if (variableMap == null) {
			variableMap = new HashMap<String, Object>();
		}
		variableMap.put(key, variable);
	}

	public Object getVariable(String key) {
		return variableMap.get(key);
	}
	
	public boolean update() {
		if (activeState == null) {
			activeState = selectNextState();
			if (activeState == null) {
				return false;
			}
			System.out.println("Selecting next task: " + activeState);
			if (!activeState.start()) {
				System.out.println(activeState + " couldn't start properly, setting task to null!");
				activeState = null;
				return false;
			}
		}
		
		if (activeState.update()) {
			activeState.finish();
			activeState = null;
		}
		
		return true;
	}
	
	public abstract State selectNextState();
}
