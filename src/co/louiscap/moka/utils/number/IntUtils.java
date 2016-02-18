/*
Copyright (c) 2015, Louis Capitanchik
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of Affogato nor the names of its associated properties or
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package co.louiscap.moka.utils.number;

/**
 * @author Louis Capitanchik
 */
public class IntUtils {
    /**
     * Finds the largest number in the sorted array of ints that is less
     * than the provided target value
     * @param target The value that should be used to find the correct lower
     * bound
     * @param bounds A sorted array of ints from which the lower bound should be
     * chosen
     * @return The largest int from the bounds array that is smaller than the
     * provided target value. If none of the numbers in the array are smaller,
     * {@link java.lang.Integer#MIN_VALUE} is returned instead.
     */
    public static int getClosestLowerBound(int target, int[] bounds) {
        int index = getClosestLowerBoundIndex(target, bounds);
        if(index > -1) {
            return bounds[index];
        } else {
            return Integer.MIN_VALUE;
        }
    }
    
    /**
     * Finds the array index of the largest number in the sorted array of ints
     * that is less than the provided target value
     * @param target The value that should be used to find the correct lower
     * bound
     * @param bounds A sorted array of ints from which the lower bound should be
     * chosen
     * @return The index in the array of the largest int that is smaller than 
     * the provided target value. If none of the numbers in the array are smaller,
     * {@code -1} is returned instead.
     */
    public static int getClosestLowerBoundIndex(int target, int[] bounds) {
        int c = bounds.length;
        while((--c) >= 0) {
            if(bounds[c] <= target) {
                return c;
            }
        }
        return -1;
    }
}
