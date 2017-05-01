/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.sdc.railmanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Rail implements Serializable {
    private final Set<Character> ocuppancy[];

    public Rail(int nSegments) {
        this.ocuppancy = new Set[nSegments];
        for(int i = 0; i < nSegments; i++) {
            this.ocuppancy[i] = new HashSet<>();
        }
    }
    
    public int getNumberSegments() {
        return this.ocuppancy.length;
    }

    public boolean haveSegment(int segment) {
        return segment >= 0 && this.getNumberSegments() >= segment;
    }

    public boolean isAccessible(char composition, int segment) {
        if (!this.haveSegment(segment)) {
            return false;
        }
        
        int prevSegment = Math.max(0, segment - 1);
        int nextSegment = Math.min(this.ocuppancy.length, segment + 1);
        
        for (int i = prevSegment; i < nextSegment; i++) {
            for(char c : this.ocuppancy[i]) {
                if( c != composition ) {
                    return false;
                }
            }
        }

        return true;
    }

    public void addPresence(char composition, int segment) {
        if (this.haveSegment(segment)) {
            this.ocuppancy[segment].add(composition);
        }
    }

    public void removePresence(char composition, int segment) {
        if (this.haveSegment(segment)) {
            this.ocuppancy[segment].remove(composition);
        }
    }

    public Map<Integer, char[]> getPositions() {
        Map<Integer, char[]> res = new HashMap<>();
        
        for(int seg = 0; seg < this.ocuppancy.length; seg++) {
            char comp[] = new char[this.ocuppancy[seg].size()];
            int i = 0;
            for( char c : this.ocuppancy[seg] ) {
                comp[i++] = c;
            }
            
            res.put(seg, comp);
        }

        return res;
    }

    @Override
    public String toString() {
        return "\tRail{ocuppancy=" + ocuppancy + '}';
    }
}
