package com.automateeverything.main;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

/**
 * Class for managing a window
 */
public class Window {

    public long window;
    public int width;
    public int height;

    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;
    GLFWFramebufferSizeCallback fbCallback;

    Consumer<Window> onResize = (win) -> {
    };
    ArrayList<BiConsumer<Window, Integer>> onKeyPress = new ArrayList<>();
    ArrayList<BiConsumer<Window, Integer>> onKeyRelease = new ArrayList<>();
    public Vector2d scroll = new Vector2d();

    /**
     * Create a window from a width, height and title
     * 
     * @param w     The width
     * @param h     The height
     * @param title The title
     */
    public Window(int w, int h, String title) {
        this.width = w;
        this.height = h;

        // Init glfw
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // create window
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        setCallbacks();

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        // Display window
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
        glfwShowWindow(window);
        
		GL.createCapabilities();
    }

    /**
     * Create callbacks for window events
     */
    private void setCallbacks() {
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
                if (action == GLFW_PRESS)
                    onKeyPress.forEach(n->n.accept(Window.this, key));
                if (action == GLFW_RELEASE)
                    onKeyRelease.forEach(n->n.accept(Window.this, key));
            }
        });
        glfwSetFramebufferSizeCallback(window, fbCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                    width = w;
                    height = h;
                    onResize.accept(Window.this);
                }
            }
        });
        glfwSetScrollCallback(window, new GLFWScrollCallbackI() {
            @Override
            public void invoke(long window, double xOffset, double yOffset) {
                scroll.add(xOffset, yOffset);
            }
        });
    }

    /**
     * Set what to do when the window is resized
     * 
     * @param onResize
     */
    public void onResize(Consumer<Window> onResize) {
        this.onResize = onResize;
        onResize.accept(this);
    }

    /**
     * Set what to do when a key is pressed
     * 
     * @param onKeyPress
     */
    public void onKeyPress(BiConsumer<Window, Integer> onKeyPress) {
        this.onKeyPress.add(onKeyPress);
    }

    /**
     * Set what to do when a key is pressed
     * 
     * @param onKeyRelease
     */
    public void onKeyRelease(BiConsumer<Window, Integer> onKeyRelease) {
        this.onKeyRelease.add(onKeyRelease);
    }

    /**
     * Return if the window should close
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * Return the width /height aspect ratio
     */
    public float getAspect() {
        return (float) width / height;
    }

    /**
     * Destroy old window, callbacks, and free gl context
     */
    public void clean() {
        try {
            glfwDestroyWindow(window);
            keyCallback.free();
        } finally {
            glfwTerminate();
            errorCallback.free();
        }
    }

    double[] xs = new double[] { 0 };
    double[] ys = new double[] { 0 };
    double[] xso = new double[] { 0 };
    double[] yso = new double[] { 0 };

    /**
     * update the window so it can give information of user input
     */
    public void update() {
        glfwGetCursorPos(window, xs, ys);
        Globals.dm = new Vector2d(xs[0] - xso[0], ys[0] - yso[0]);
        xso = xs.clone();
        yso = ys.clone();
    }

    /**
     * Swap buffers and poll events
     */
    public void swapBuffers() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }
}