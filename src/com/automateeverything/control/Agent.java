package com.automateeverything.control;

import java.util.ArrayList;

import com.automateeverything.control.systems.agent.AgentSystem;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

/**
 * Agent
 */
public class Agent {
    private ArrayList<AgentSystem> controls = new ArrayList<>();
    private Object3D agent;

    public Agent(Object3D agent) {
        this.agent = agent;
    }

    public void update(Window window) {
        controls.forEach(n -> n.run(agent, window));
    }

    public void addControl(AgentSystem control) {
        controls.add(control);
    }
    
}