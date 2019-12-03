package com.automateeverything.control.systems.agent;

import java.util.ArrayList;

import com.automateeverything.control.Agent;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.joml.Vector3f;

/**
 * MoveRight
 */
public class ReverseTime implements AgentSystem {

  ArrayList<Vector2> locs = new ArrayList<>();
  ArrayList<Vector2> speeds = new ArrayList<>();
  ArrayList<Vector2> forces = new ArrayList<>();

  @Override
  public void run(Agent agent, Window window) {
    locs.add(agent.agent.collider.getChangeInPosition().copy());
    speeds.add(agent.agent.collider.getLinearVelocity().copy());
    forces.add(agent.agent.collider.getForce().copy());
    undoStep(agent.agent, window);
    undoStep(agent.agent, window);
  }

  private void undoStep(Object3D agent, Window window) {
    if (Globals.inputmap.get("time", window) && speeds.size() > 1) {
      agent.getPos().add(new Vector3f(0, (float) -locs.get(locs.size() - 1).y, (float) locs.get(locs.size() - 1).x));
      agent.collider.shift(locs.get(locs.size() - 1).multiply(-1));
      locs.remove(locs.size() - 1);
      agent.collider.setLinearVelocity(speeds.get(speeds.size() - 1));
      speeds.remove(speeds.size() - 1);
      agent.collider.clearAccumulatedForce();
      agent.collider.applyForce(forces.get(forces.size()-1).multiply(-1));
      forces.remove(forces.size() - 1);
    }
  }

}