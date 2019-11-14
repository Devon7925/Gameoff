varying vec4 vertColor;
uniform mat4 matr;

void main(){
    vec4 local = matr*gl_Vertex;
    gl_Position = gl_ProjectionMatrix*local;
    vertColor = vec4(gl_Vertex.x+0.5, gl_Vertex.y+0.5, gl_Vertex.z+0.5, 1.0);
}