package toldea.romecraft.entity.ai.fsm;

public abstract class State {
	protected StateMachine stateMachine;

	public void linkStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public abstract void start();

	public abstract boolean update();

	public abstract void finish();
}
