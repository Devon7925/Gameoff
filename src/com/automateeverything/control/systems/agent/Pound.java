package com.automateeverything.control.systems.agent;

import com.automateeverything.control.Agent;
import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.geometry.Vector2;

/**
 * Pound
 */
public class Pound implements AgentSystem {

    public Pound(Window window, Object3D agent) {
        window.onKeyPress((win, key) -> {
            if (Globals.inputmap.get("pound", win))
                agent.collider.applyImpulse(new Vector2(-0.8*agent.collider.getMass().getMass()*agent.collider.getLinearVelocity().x, 0));
        });
    }

    @Override
    public void run(Agent agent, Window window) {
        if(Globals.inputmap.get("pound", window)){
            agent.gravityScale *= 10;
            agent.friction /= 2;
        }
    }

    
}