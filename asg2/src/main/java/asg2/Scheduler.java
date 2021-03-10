package asg2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Scheduler {

    private List<Server> servers = new ArrayList<Server>(100);

    //initializes all the servers
    public Scheduler(int maxNoServers) {
        for (int i = 1; i <= maxNoServers; i++) {
            Server s = new Server();
            s.setId(i);
            servers.add(s);
            Thread tr = new Thread(s);
            tr.start();
        }
    }
    
    public String display() {
    	String s = new String();
    	for (Server serv: servers) {
    		s = s + "Queue " + serv.getId() + ": ";
    		BlockingQueue<Client> clients = serv.getClients();
    		if (clients.isEmpty()) {
    			s = s + "Closed";
    		}
    		else {
    			s = s + serv;
    		}
    		s = s + "\n";
    	}
    	return s;
    }
    
    //get the server with the minimum waitingPeriod, in order to put the newly arrived client there
    private int getMinWaitingTimeServer() {
    	int min = 9999;
    	int id = 0;
    	for (Server s: servers) {
    		if (s.getWaitingPeriod() < min) {
    			min = s.getWaitingPeriod();
    			id = s.getId();
    		}
    	}
    	return id;
    }
    
    //sets all the servers running instance variable to false; is used for the moment when the simulation has to stop
    public void closeServers() {
    	for (Server s: servers) {
    		s.setRunning(false);
    	}
    }
    
    //returns a boolean value indicating whether the servers are all closed or not
    public boolean areServersClosed() {
    	boolean flag = true; //changes if any of the servers is open
    	for (Server s: servers) {
    		if (!s.getClients().isEmpty()) {
    			flag = false;
    			break;
    		}
    	}
    	return flag; //returns true if all the servers are closed 
    }

    //places the newly arrived client to the server with the minimum waitingPeriod
    public void dispatchClient(Client c) {
        Collections.sort(servers);
        servers.get(getMinWaitingTimeServer() - 1).addClient(c);
    }

}

