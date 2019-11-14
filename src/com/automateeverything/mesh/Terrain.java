package com.automateeverything.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.function.BiFunction;

import com.automateeverything.control.Player;
import com.automateeverything.main.Shader;

import org.joml.Matrix4f;

/**
 * Terrain
 */
public class Terrain {
    final private float[][] heights;
    final public Shader shader;
    float d;
    int size;
    boolean shading = false;
    BiFunction<Long, Long, Float> generator;

    public Terrain(float d, int size, Shader shader, BiFunction<Long, Long, Float> generator) {
        this.d = d;
        this.size = size;
        this.heights = new float[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                heights[x][y] = generator.apply((long) x, (long) y);
            }
        }
        this.shader = shader;
        this.generator = generator;
    }

    public void render(Player player, Matrix4f modelMatrix, Matrix4f modelViewMatrix) {
        if (!shading) {
            shader.create();
            shading = true;
        }
        shader.bind();
        shader.setUniform("matr", player.getViewMatrix().mul(modelMatrix, modelViewMatrix));
        glBegin(GL_TRIANGLES);
        for (int xi = 0; xi < size-1; xi++)
            for (int zi = 0; zi < size-1; zi++){
                glVertex3f(xi * d, heights[xi][zi], zi * d);
                zi++;
                glVertex3f(xi * d, heights[xi][zi], zi * d);
                xi++;
                glVertex3f(xi * d, heights[xi][zi], zi * d);
                glVertex3f(xi * d, heights[xi][zi], zi * d);
                zi--;
                glVertex3f(xi * d, heights[xi][zi], zi * d);
                xi--;
                glVertex3f(xi * d, heights[xi][zi], zi * d);
            }
        glEnd();
        shader.unbind();
    }
}