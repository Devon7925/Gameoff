package com.automateeverything.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.automateeverything.control.Player;
import com.automateeverything.main.Shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Mesh
 */
public class Mesh {
    final private ArrayList<Vector3f> verticies;
    final private ArrayList<Face> faces;
    final public Shader shader;
    boolean shading = false;

    public Mesh(Vector3f[] verticies, Face[] faces) {
        this.verticies = new ArrayList<>(Arrays.asList(verticies));
        this.faces = new ArrayList<>(Arrays.asList(faces));
        shader = new Shader("resources/shaders/screen.vert", "resources/shaders/screen.frag");
    }

    public Mesh(String objPath, Shader shader) {
        this.verticies = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.shader = shader;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(objPath)));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                if (split[0].equals("v")) {
                    verticies.add(new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]),
                            Float.parseFloat(split[3])));
                } else if (split[0].equals("f")) {
                    if (split.length > 4) {
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - 1,
                                        Integer.parseInt(split[2].split("/")[0]) - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - 1));
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - 1,
                                        Integer.parseInt(split[4].split("/")[0]) - 1));
                    } else
                        faces.add(
                                new Face(
                                        new Vector3f((float) Math.random(), (float) Math.random(),
                                                (float) Math.random()),
                                        Integer.parseInt(split[1].split("/")[0]) - 1,
                                        Integer.parseInt(split[2].split("/")[0]) - 1,
                                        Integer.parseInt(split[3].split("/")[0]) - 1));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Player player, Matrix4f modelMatrix, Matrix4f modelViewMatrix) {
        if(!shading){
            shader.create();
            shading = true;
        }
        shader.bind();
        shader.setUniform("matr", player.getViewMatrix().mul(modelMatrix, modelViewMatrix));
        glBegin(GL_TRIANGLES);
        for (Face face : faces) {
            glColor3f(face.getColor().x, face.getColor().y, face.getColor().z);
            face.getIndicies().stream().map(i -> verticies.get(i)).forEach(n -> glVertex3f(n.x, n.y, n.z));
        }
        glEnd();
        shader.unbind();
    }
}