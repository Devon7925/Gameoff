package com.automateeverything.control.systems.agent;

import com.automateeverything.control.Agent;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;

import org.dyn4j.geometry.Vector2;

/**
 * MoveRight
 */
public class MoveLeft implements AgentSystem {

  @Override
  public void run(Agent agent, Window window) {
    if (Globals.inputmap.get("left", window))
      agent.agent.collider.applyForce(new Vector2(-1.5, 0));
  }

}