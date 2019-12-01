package com.automateeverything.control.systems.agent;

import com.automateeverything.control.Agent;
import com.automateeverything.main.Window;

/**
 * ControlSystem
 */
public interface AgentSystem {
    public void run(Agent agent, Window window);
}