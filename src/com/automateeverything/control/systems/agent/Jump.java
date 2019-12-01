package com.automateeverything.control.systems.agent;

import com.automateeverything.control.Agent;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.geometry.Vector2;

/**
 * Jump
 */
public class Jump implements AgentSystem {
    public Jump(Window window, Object3D agent) {
        window.onKeyPress((win, key) -> {
            if (Globals.inputmap.get("jump", win))
                agent.collider.applyForce(new Vector2(0, 300));
        });
    }

    @Override
    public void run(Agent agent, Window window) {
    }
    
}