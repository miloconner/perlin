package util;

public class Vec2 {
  private double x, y;
  public Vec2(double _x, double _y) {
    this.x = _x;
    this.y = _y;
  }
  public Vec2() {
    this.x = 0;
    this.y = 0;
  }
  public static Vec2 random(double mag) {
    return new Vec2((Math.random()-0.5)*mag, (Math.random()-0.5)*mag);
  }

  // public Vec3 toVec3() {
  //   return new Vec3(this.x, this.y, 0);
  // }

  public Vec2 clone() {
    return new Vec2(this.x, this.y);
  }
  public double getX() {
    return this.x;
  }
  public double getY() {
    return this.y;
  }

  public Vec2 negative() {
    return new Vec2(-x, -y);
  }

  public String toString() {
    return this.x + ", " + this.y;
  }

  public Vec2 add(Vec2 other) {
    return new Vec2(this.x + other.x, this.y + other.y);
  }
  public void set(double _x, double _y) {
    this.x = _x; this.y = _y;
  }
  public void addThis(Vec2 other) {
    this.x += other.x;
    this.y += other.y;
  }

  public double dot(Vec2 other) {
    return this.x*other.x + this.y*other.y;
  }

  public void transformThis(double theta, double cX, double cY, double oX, double oY) { //cY is stretch of Y, 1 is same stretch
    Vec2 tmp = this.clone();
    tmp.x = ((this.x - oX) * Math.cos(theta) - (this.y - oY) * Math.sin(theta)) * cX;
    tmp.y = ((this.x - oX) * Math.sin(theta) + (this.y - oY) * Math.cos(theta))  * cY;
    this.x = tmp.x + oX; this.y = tmp.y + oY;
  }
}
