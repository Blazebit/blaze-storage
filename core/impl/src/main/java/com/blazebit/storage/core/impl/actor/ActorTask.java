package com.blazebit.storage.core.impl.actor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ActorTask implements Runnable {

	private final AbstractActor actor;
	private final Logger LOG;

	public ActorTask(AbstractActor actor) {
		this.actor = actor;
		this.LOG = Logger.getLogger(actor.getClass().getName());
	}

	@Override
	public void run() {
		try {
			actor.work();
		} catch (Exception ex) {
			LOG.log(Level.SEVERE, "Error when running actor!", ex);
		}
	}

}