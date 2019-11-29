package com.automateeverything.control.systems.agent;

import com.automateeverything.main.Globals;
import com.automateeverything.main.Window;
import com.automateeverything.mesh.Object3D;

import org.dyn4j.geometry.Vector2;

/**
 * Jump
 */
public class BetterJump implements AgentSystem {
    public float fallMultiplier = 1f;
    public float lowJumpMultiplier = 0.2f;

    public BetterJump(Window window, Object3D agent) {
        window.onKeyPress((win, key) -> {
            if (Globals.inputmap.get("jump", win))
                agent.collider.applyForce(new Vector2(0, 300));
        });
    }

    @Override
    public void run(Object3D agent, Window window) {
        agent.collider.setGravityScale(fallMultiplier);
        if (Globals.inputmap.get("jump", window) && agent.collider.getLinearVelocity().y > 0) {
            agent.collider.setGravityScale(lowJumpMultiplier);
        }
    }

}