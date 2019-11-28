package com.automateeverything.control.systems.agent;

import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

/**
 * ControlSystem
 */
public interface AgentSystem {
    public void run(Object3D agent, Window window);
}