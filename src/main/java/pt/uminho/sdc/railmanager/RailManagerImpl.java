/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.sdc.railmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RailManagerImpl implements RailManager, Serializable {

    public Map<String, Rail> rails;
    List<String> alarms;

    public RailManagerImpl() {
        this.rails = new HashMap<>();
        this.alarms = new ArrayList<>();
    }

    @Override
    public boolean access(String line, int segment, char composition) {
        if (this.rails.containsKey(line)) {
            Rail rail = this.rails.get(line);
            return rail.isAccessible(composition, segment);
        }

        return false;
    }

    @Override
    public boolean enter(String line, int segment, char composition) {
        boolean res = true;
        if (!this.access(line, segment, composition)) {
            this.alarms.add("L" + line + "S" + segment + "C" + composition);
            res = false;
        }

        if (this.rails.containsKey(line)) {
            Rail rail = this.rails.get(line);
            rail.addPresence(composition, segment);
        }
        
        return res;
    }

    @Override
    public void leave(String line, int segment, char composition) {
        if (!this.rails.containsKey(line)) {
            return;
        }

        Rail rail = this.rails.get(line);
        rail.removePresence(composition, segment);
    }

    @Override
    public Map<Integer, char[]> getPositions(String line) {
        Rail rail = this.rails.get(line);
        return rail.getPositions();
    }

    @Override
    public List<String> getAlarms() {
        List<String> res = new ArrayList<>();

        for (String alarm : this.alarms) {
            res.add(alarm);
        }

        return res;
    }
    
    @Override
    public Map<String, Integer> getRails() {
        Map<String, Integer> res = new HashMap<>();
        
        for(String railName : this.rails.keySet()) {
            res.put(railName, this.rails.get(railName).getNumberSegments());
        }
        
        return res;
    }

    public void addRail(String name, Rail rail) {
        this.rails.put(name, rail);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Rails [").append(this.rails.size()).append("]\n");
        for(String railName : this.rails.keySet()) {
            Rail r = this.rails.get(railName);
            sb.append(railName).append("\t");
            sb.append(r.toString()).append('\n');
        }
        
        sb.append("Alamrs [").append(this.alarms.size()).append("]\n");
        for(String alarm : this.alarms) {
            sb.append(alarm).append('\n');
        }
        
        return sb.toString();
    }
}
