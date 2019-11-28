package com.automateeverything.control.systems.agent;

import java.util.ArrayList;

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

  public ReverseTime(Window window, Object3D agent) {
    window.onKeyRelease((win, key) -> {
      if (Globals.inputmap.getID("time") == key)
        agent.collider.setLinearVelocity(speeds.get(speeds.size() - 1));
        agent.collider.setMass(MassType.FIXED_ANGULAR_VELOCITY);
        agent.collider.applyForce(new Vector2(0, 0));
    });
  }

  @Override
  public void run(Object3D agent, Window window) {
    locs.add(agent.collider.getChangeInPosition().copy());
    speeds.add(agent.collider.getLinearVelocity().copy());
    undoStep(agent, window);
    undoStep(agent, window);
  }

  private void undoStep(Object3D agent, Window window) {
    if (Globals.inputmap.get("time", window) && speeds.size() > 1) {
      agent.collider.setMass(MassType.INFINITE);
      agent.getPos().add(new Vector3f(0, (float) -locs.get(locs.size() - 1).y, (float) locs.get(locs.size() - 1).x));
      agent.collider.shift(locs.get(locs.size() - 1).multiply(-1));
      locs.remove(locs.size() - 1);
      agent.collider.setLinearVelocity(new Vector2(0, 0));
      agent.collider.clearAccumulatedForce();
      speeds.remove(speeds.size() - 1);
    }
  }

}