package asg2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable, Comparable<Server>{

    private BlockingQueue<Client>  clients = new ArrayBlockingQueue<Client>(100);
    private AtomicInteger waitingPeriod; //updates each second
    private int id; //identifier
    private boolean running; //true if the server is open, false otherwise

	public Server() {
        this.waitingPeriod = new AtomicInteger(0); //initial condition
        this.running = true; 
    }

	//computes the waitingTime of the client and the new waitingPeriod for the server
    public void addClient(Client client) {
        this.clients.add(client);
        client.setWaitingTime(this.getWaitingPeriod() + client.getProcessingTime());
        this.waitingPeriod.set(client.getWaitingTime());
    }

    //@Override
    public void run() {
        while (running) {
            try {
                if (clients.isEmpty()) {
                	Thread.sleep(1000); //thread waits until there is a client in the queue
                }
                else {
                	clients.element().setProcessingTime(clients.element().getProcessingTime()-1);
                	setWaitingPeriod(getWaitingPeriod() - 1);
                	if (clients.element().getProcessingTime() > 0) {
                		Thread.sleep(1000);
                    }
                	else {
                		clients.remove(); //when the client has been processed; the processing time decremented to 0
                		Thread.sleep(1000);
                	}
                	//setWaitingPeriod(getWaitingPeriod() - 1);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String toString() {
    	String s = new String();
    	for (Client c: clients) {
    		s = s + c.toString() + " ";
    	}
    	return s;
    }

    public BlockingQueue<Client> getClients() {
        return this.clients;
    }

    public int getWaitingPeriod() {
        return this.waitingPeriod.get();
    }
    
    public void setWaitingPeriod(int n) {
        this.waitingPeriod = new AtomicInteger(n);
    }

    public int getId() {
 		return id;
 	}

 	public void setId(int id) {
 		this.id = id;
 	}
 	
 	public void setRunning(boolean running) {
 		this.running = running;
 	}
    
    //@Override
    public int compareTo(Server o) {
      	return this.id - o.getId();
    }
}

