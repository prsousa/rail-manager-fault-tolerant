/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.sdc.railmanager;

import java.util.List;
import java.util.Map;
import pt.uminho.sdc.cs.RemoteInvocationException;

public interface RailManager {
    boolean access(String line, int segment, char composition) throws RemoteInvocationException;
    boolean enter(String line, int segment, char composition) throws RemoteInvocationException;
    void leave(String line, int segment, char composition) throws RemoteInvocationException;
    Map<Integer, char[]> getPositions(String line) throws RemoteInvocationException;
    List<String> getAlarms() throws RemoteInvocationException;
    Map<String, Integer> getRails() throws RemoteInvocationException;
}
