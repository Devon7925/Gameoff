package com.automateeverything.control.systems.agent;

import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.geometry.Vector2;

/**
 * MoveRight
 */
public class MoveRight implements AgentSystem {

    @Override
    public void run(Object3D agent, Window window) {
        if (Globals.inputmap.get("right", window))
            agent.collider.applyForce(new Vector2(1.5, 0));
    }

}