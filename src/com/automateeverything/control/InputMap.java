package com.automateeverything.control;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import java.util.HashMap;
import java.util.Map;

import com.automateeverything.main.Window;

/**
 * KeyMap
 */
public class InputMap {
    Map<String, Integer> inputmap = new HashMap<>();
    Map<String, InputType> typemap = new HashMap<>();

    public boolean get(String key, Window window) {
        if(!inputmap.containsKey(key)) throw new IllegalArgumentException("Key \""+key+"\" does not exist");
        switch (typemap.get(key)) {
            case KEY:
                return glfwGetKey(window.window, inputmap.get(key)) == GLFW_PRESS;
            case MOUSE:
                return glfwGetMouseButton(window.window, inputmap.get(key)) == GLFW_PRESS;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getID(String key){
        return inputmap.get(key);
    }

    public void put(String key, Integer value, InputType type) {
        inputmap.put(key, value);
        typemap.put(key, type);
    }
}