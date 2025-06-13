package perlin;

// JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
public class ImprovedNoise {

        /**
         * This is the permutation table {@link #p}.
         */
        public static final int p[] = new int[512], permutation[] = {
                        151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69,
                        142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219,
                        203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175,
                        74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230,
                        220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209,
                        76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198,
                        173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212,
                        207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44,
                        154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79,
                        113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12,
                        191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157,
                        184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29,
                        24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };
        static {
                for (int i = 0; i < 256; i++) {
                        p[256 + i] = p[i] = permutation[i];
                }
        }

        /**
         * <h2>What "Works" Means for a Fade Function in Perlin Noise</h2>
         * <p>
         * A fade function "works" well if it helps to create smooth, continuous noise
         * without visible artifacts (like creases or sharp edges) at the boundaries of
         * the grid cells. This smoothness is often characterized by the continuity of
         * the noise function and its derivatives.
         * </p>
         * <ul>
         * <li>
         * <b>Classic Perlin Noise (using 3t² - 2t³):</b>
         * <br>
         * This fade function results in noise that is <b>C1 continuous</b>.
         * The noise function itself is continuous, and its first derivative (rate of
         * change, or slope)
         * is also continuous across cell boundaries.
         * </li>
         * <li>
         * <b>Improved Perlin Noise (using 6t⁵ - 15t⁴ + 10t³):</b>
         * <br>
         * This fade function results in noise that is <b>C2 continuous</b>.
         * The noise function, its first derivative, <i>and</i> its second derivative
         * are all continuous.
         * This leads to even smoother results with fewer directional artifacts.
         * </li>
         * </ul>
         * <p>
         * <b>Key Mathematical Properties Required:</b>
         * For a fade function f(t) to be effective in reducing artifacts at t=0 and t=1
         * (the cell boundaries),
         * it generally needs to satisfy these conditions:
         * </p>
         * <ol>
         * <li><b>f(0) = 0:</b> At the start of the interpolation interval, the weight
         * should be 0.</li>
         * <li><b>f(1) = 1:</b> At the end of the interpolation interval, the weight
         * should be 1.</li>
         * <li><b>f'(0) = 0:</b> (First derivative at t=0 is zero) This is crucial. It
         * means
         * the rate of change of the interpolation weight is zero as you enter a new
         * cell from one side. This helps eliminate creases.</li>
         * <li><b>f'(1) = 0:</b> (First derivative at t=1 is zero) Similarly, the rate
         * of
         * change is zero as you leave the cell on the other side.</li>
         * </ol>
         * <p>
         * For even smoother C2 continuity (as in improved Perlin noise), you'd also
         * need:
         * </p>
         * <ol>
         * <li><b>f''(0) = 0:</b> (Second derivative at t=0 is zero)</li>
         * <li><b>f''(1) = 0:</b> (Second derivative at t=1 is zero)</li>
         * </ol>
         * <h2>This particular fade function</h2>
         * <p>
         * <b>6t⁵ - 15t⁴ + 10t³</b> (Improved Perlin Fade / Quintic Hermite
         * Interpolation)<br>
         * The classic Perlin fade is: <b>3t² - 2t³</b>
         * </p>
         * <p>
         * <b>Check of f(t) = 6t⁵ - 15t⁴ + 10t³</b>
         * </p>
         * <ul>
         * <li>f(0) = 0 (OK)</li>
         * <li>f(1) = 6 - 15 + 10 = 1 (OK)</li>
         * <li>f'(t) = 30t⁴ - 60t³ + 30t²</li>
         * <ul>
         * <li>f'(0) = 0 (OK)</li>
         * <li>f'(1) = 30 - 60 + 30 = 0 (OK)</li>
         * </ul>
         * <li>f''(t) = 120t³ - 180t² + 60t</li>
         * <ul>
         * <li>f''(0) = 0 (OK - leads to C2 continuity)</li>
         * <li>f''(1) = 120 - 180 + 60 = 0 (OK - leads to C2 continuity)</li>
         * </ul>
         * </ul>
         * <p>
         * This function works better by ensuring the second derivative is also zero at
         * the boundaries, leading to smoother results.
         * </p>
         *
         * @param t the value to fade, it must be in the range 0 <= t < 1
         * @return the faded value.
         */
        public static double fade(double t) {
                return t * t * t * ((t * ((t * 6) - 15)) + 10);
        }

        /**
         * Return a gradient direction in the form of a double based on the lower 4-bits
         * of the
         * specified hash code for the specified 3D coordinate. As follows:
         * 
         * <pre>
         *  grad( 0 : 0000) u=x, v=y, left= x, right= y, result=  x +  y -- 1
         *  grad( 1 : 0001) u=x, v=y, left=-x, right= y, result= -x +  y -- 2
         *  grad( 2 : 0010) u=x, v=y, left= x, right=-y, result=  x + -y
         *  grad( 3 : 0011) u=x, v=y, left=-x, right=-y, result= -x + -y
         *  grad( 4 : 0100) u=x, v=z, left= x, right= z, result=  x +  z
         *  grad( 5 : 0101) u=x, v=z, left=-x, right= z, result= -x +  z
         *  grad( 6 : 0110) u=x, v=z, left= x, right=-z, result=  x + -z
         *  grad( 7 : 0111) u=x, v=z, left=-x, right=-z, result= -x + -z
         *  grad( 8 : 1000) u=y, v=z, left= y, right= z, result=  y +  z
         *  grad( 9 : 1001) u=y, v=z, left=-y, right= z, result= -y +  z -- 3
         *  grad(10 : 1010) u=y, v=z, left= y, right=-z, result=  y + -z
         *  grad(11 : 1011) u=y, v=z, left=-y, right=-z, result= -y + -z -- 4
         *  grad(12 : 1100) u=y, v=x, left= y, right= x, result=  y +  x -- 1
         *  grad(13 : 1101) u=y, v=z, left=-y, right= z, result= -y +  z -- 3
         *  grad(14 : 1110) u=y, v=x, left= y, right=-x, result=  y + -x -- 2
         *  grad(15 : 1111) u=y, v=z, left=-y, right=-z, result= -y + -z -- 4
         * </pre>
         * 
         * @param hash the hash code
         * @param x    the x component of the coordinate
         * @param y    the y component of the coordinate
         * @param z    the z component of the coordinate
         * @return
         */
        public static double gradient(int hash, double x, double y, double z) {
                int h = hash & 0x0000000F; // CONVERT LO 4 BITS OF HASH CODE
                double u = h < 8 ? x : y; // INTO 12 GRADIENT DIRECTIONS.
                double v = h < 4 ? y : (h == 12) || (h == 14) ? x : z;
                return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }

        /**
         * <h2>linear interpolation</h2> Takes three arguments: a, b, and t. It
         * calculates a value
         * that lies between a and b, based on the percentage t specified. If t is 0,
         * the result is
         * a; if t is 1, the result is b; and any value between 0 and 1 will give a
         * linear
         * interpolation between a and b.
         * 
         * @param t this should be between 0.0 and 1.0 inclusive.
         * @param a
         * @param b
         * @return
         */
        public static double lerp(double t, double a, double b) {
                return a + (t * (b - a));
        }

        /**
         * This takes in a 3D coordinate and returns a single noise value for it.
         * 
         * @param x the x component of the coordinate
         * @param y the y component of the coordinate
         * @param z the z component of the coordinate
         * @return the noise value for this coordinate
         */
        public static double noise(double x, double y, double z) {
                /*
                 * Get the integer part of each coordinate component, this is a corner of the
                 * unit cube
                 * that contains the coordinate.
                 */
                int xInt = (int) Math.floor(x) & 255, yInt = (int) Math.floor(y) & 255,
                                zInt = (int) Math.floor(z) & 255;
                /*
                 * Get the fractional part of each coordinate component
                 */
                double xFrac = x - Math.floor(x);
                double yFrac = y - Math.floor(y);
                double zFrac = z - Math.floor(z);
                /*
                 * FIND FADE CURVES FOR EACH OF X,Y,Z. These values are used to weight the
                 * contribution of
                 * gradient
                 */
                double xFade = fade(xFrac), //
                                yFade = fade(yFrac), //
                                zFade = fade(zFrac);
                /*
                 * Hash coordinates of the 8 cube corners,
                 */
                int A = p[xInt] + yInt, //
                                AA = p[A] + zInt, //
                                AB = p[A + 1] + zInt, //
                                B = p[xInt + 1] + yInt, //
                                BA = p[B] + zInt, //
                                BB = p[B + 1] + zInt; //
                return lerp(zFade, //
                                lerp(yFade, //
                                                lerp(xFade, gradient(p[AA], xFrac, yFrac, zFrac), // AND ADD
                                                                gradient(p[BA], xFrac - 1, yFrac, zFrac)), // BLENDED
                                                lerp(xFade, gradient(p[AB], xFrac, yFrac - 1, zFrac), // RESULTS
                                                                gradient(p[BB], xFrac - 1, yFrac - 1, zFrac))), // FROM
                                                                                                                // 8
                                lerp(yFade, //
                                                lerp(xFade, gradient(p[AA + 1], xFrac, yFrac, zFrac - 1), // CORNERS
                                                                gradient(p[BA + 1], xFrac - 1, yFrac, zFrac - 1)), // OF
                                                                                                                   // CUBE
                                                lerp(xFade, gradient(p[AB + 1], xFrac, yFrac - 1, zFrac - 1),
                                                                gradient(p[BB + 1], xFrac - 1, yFrac - 1, zFrac - 1))));
        }
}
