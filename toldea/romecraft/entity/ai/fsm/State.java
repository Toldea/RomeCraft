package toldea.romecraft.entity.ai.fsm;

public abstract class State {
	protected StateMachine stateMachine;

	public void linkStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public abstract boolean start();

	/**
	 * Update the state. Should return true when the state is finished or false when it should continue executing.
	 * @return
	 */
	public abstract boolean update();

	public abstract void finish();
}
