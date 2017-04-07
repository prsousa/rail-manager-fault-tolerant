/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.sdc.railmanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class Rail implements Serializable {

    private final int nSegments;
    private final Map<Character, Integer> ocuppancy;

    public Rail(int nSegments) {
        this.nSegments = nSegments;
        this.ocuppancy = new HashMap<>();
    }

    public boolean haveSegment(int segment) {
        return segment >= 0 && this.nSegments >= segment;
    }

    public boolean isAccessible(char composition, int segment) {
        if (!this.haveSegment(segment)) {
            return false;
        }

        for (char c : this.ocuppancy.keySet()) {
            int p = this.ocuppancy.get(c);
            if (c != composition && Math.abs(p - segment) <= 1) {
                return false;
            }
        }

        return true;
    }

    public void addPresence(char composition, int segment) {
        if (this.haveSegment(segment)) {
            this.ocuppancy.put(composition, segment);
        }
    }

    public void removePresence(char composition, int segment) {
        this.ocuppancy.remove(composition, segment);
    }

    public Map<Character, Integer> getPositions() {
        Map<Character, Integer> res = new HashMap<>();

        for (char c : this.ocuppancy.keySet()) {
            int position = this.ocuppancy.get(c);
            res.put(c, position);
        }

        return res;
    }

    @Override
    public String toString() {
        return "\tRail{" + "nSegments=" + nSegments + ", ocuppancy=" + ocuppancy + '}';
    }
}
