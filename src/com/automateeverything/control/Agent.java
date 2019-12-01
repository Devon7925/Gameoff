package com.automateeverything.control;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.automateeverything.control.systems.agent.AgentSystem;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

/**
 * Agent
 */
public class Agent {
    private ArrayList<AgentSystem> controls = new ArrayList<>();
    public Object3D agent;
    public double gravityScale;
    public double normalFriction, friction;

    public Agent(Object3D agent) {
        this.agent = agent;
        normalFriction = agent.collider.getFixtures().stream().collect(Collectors.averagingDouble(n -> n.getFriction()));
    }

    public void update(Window window) {
        gravityScale = 1;
        friction = normalFriction;
        controls.forEach(n -> n.run(this, window));
        agent.collider.setGravityScale(gravityScale);
        agent.collider.getFixtures().forEach(n -> n.setFriction(friction));
    }

    public void addControl(AgentSystem control) {
        controls.add(control);
    }
    
}