/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Math;

import Scene.Material;
import Scene.Shader;
import Scene.Colour;


/**
 * This class handles a Triangle
 * @author htrefftz
 */
public class Triangle implements Intersectable {
    /** Vertex 1 */
    Point v1;
    /** Vertex 2 */
    Point v2;
    /** Vertex 3 */
    Point v3;
    
    /** Parameter a in the plane equation */
    double a;
    /** Parameter b in the plane equation */
    double b;
    /** Parameter c in the plane equation */
    double c;
    /** Parameter d in the plane equation */
    double d;

    /** Normal vector of this triangle */
    Vector4 normal;
    
    /** Material of the triangle */
    Material material;

    double beta;
    double gamma;

    /**
     * Construct a Triangle given the three vertices that form it.
     * @param v1 first vertex
     * @param v2 second vertex
     * @param v3 third vertex
     */
    public Triangle(Point v1, Point v2, Point v3, Material material) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.material = material;
        
        Vector4 vec1 = new Vector4(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
        Vector4 vec2 = new Vector4(v3.x - v2.x, v3.y - v2.y, v3.z - v2.z);
        Vector4 n = Vector4.crossProduct(vec1, vec2);
        a = n.getX();
        b = n.getY();
        c = n.getZ();
        d = -(a * v1.x + b * v1.y + c * v1.z);
        
        normal = new Vector4(a, b, c);
        normal.normalize();
    }

    public Triangle(Point v1, Point v2, Point v3) {
        this(v1, v2, v3, new Material());
    }
    
    
    /**
     * Returns the solution intersecting this triangle with a given ray
     * @param ray Ray to intersect the triangle with
     * @return solutions to the ray-triangle intersection.
     */
    @Override
    public Solutions intersect(Ray ray) {
        ThreeByThreeSystem equation = new ThreeByThreeSystem(
                new double[][]{
                    {ray.u.getX(), this.v1.getX() - this.v2.getX(), this.v1.getX() - this.v3.getX()},
                    {ray.u.getY(), this.v1.getY() - this.v2.getY(), this.v1.getY() - this.v3.getY()},
                    {ray.u.getZ(), this.v1.getZ() - this.v2.getZ(), this.v1.getZ() - this.v3.getZ()}},
                new double[]{this.v1.getX() - ray.p0.getX(),
                    this.v1.getY() - ray.p0.getY(),
                    this.v1.getZ() - ray.p0.getZ()});

        double[] results = equation.computeSystem();

        double S = results[0];
        beta = results[1];
        gamma = results[2];
        double alpha = 1 - beta - gamma;
    
        if ((alpha > 0 && alpha < 1) && (beta > 0 && beta < 1) && (gamma > 0 && gamma < 1)) {
            return new Solutions(1, S, 0);
        } else {
            return new Solutions(0, 0, 0);
        }
    }
    
    /**
     * Returns the material of this triangle
     * @return material of this triangle
     */
    @Override
    public Material getMaterial() {
        return material;
    }
    
    /**
     * Set the material of this triangle
     * @param material Material of this triangle
     */
    public void setMaterial(Material material) {
        this.material = material;
    }
    
    /**
     * Calls the shader at the point of intersection
     * @param ray Ray that intersects this triangle
     * @param minT Value of the parameter at the intersection
     * @return Color of this triangle at the intersection
     */
    @Override
    public Colour callShader(Ray ray, double minT) {
        Point point = ray.evaluate(minT);
        UVCoordinates uvCoordinates = computeUV(point);
        return Shader.computeColor(point, normal, uvCoordinates, material);        
    }
    
    /**
     * Returns the normal of this triangle
     * @return Normal of this triangle
     */
    public Vector4 computeNormal(Point p) {
        return normal;
    }
    
     public UVCoordinates computeUV(Point p) {
        return new UVCoordinates(beta, gamma);
        
    }

    @Override
    public String toString() {
        return "Triangle{" + "v1=" + v1 + ", v2=" + v2 + ", v3=" + v3 + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + '}';
    }

    
    public static void main(String [] args) {
        Point p1 = new Point(-1, -1, -5);
        Point p2 = new Point(+3, -1, -5);
        Point p3 = new Point(-1, +3, -5);
        Ray ray1 = new Ray(new Point(0, 0, 0), new Vector4(0, 0, -1));
        Ray ray2 = new Ray(new Point(5, 5, 0), new Vector4(0, 0, -1));
        Ray ray3 = new Ray(new Point(-5, 0, 0), new Vector4(1, 0, 0));
        Triangle triangle = new Triangle(p1, p2, p3, new Material());
        Solutions solution = triangle.intersect(ray3);
        System.out.println("Intersección: " + ray3.evaluate(solution.t1));
    }
}
