package asg2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SimulationManager implements Runnable {

	private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int numberOfServers;
    private int numberOfClients;

    private List<Client> clients = new ArrayList<Client>(100);
    private Scheduler scheduler;
    
    private File input;
    private File output;
	private Scanner scan;
	private FileWriter writer;

	//reads and generates the data
    public SimulationManager(String inputs, String outputs) throws IOException {
        this.input = new File(inputs);
		this.output = new File(outputs);
		output.createNewFile();
      
		scan = new Scanner(input);
		numberOfClients = scan.nextInt();
		numberOfServers = scan.nextInt();
		timeLimit = scan.nextInt();
		String arrivalTime = scan.next();
		String[] splitArrivalTime = arrivalTime.split(",");
		minArrivalTime = Integer.parseInt(splitArrivalTime[0]);
		maxArrivalTime = Integer.parseInt(splitArrivalTime[1]);
		String processingTime = scan.next();
		String[] splitProcessingTime = processingTime.split(",");
		minProcessingTime = Integer.parseInt(splitProcessingTime[0]);
		maxProcessingTime = Integer.parseInt(splitProcessingTime[1]);
		scan.close();
		
		scheduler = new Scheduler(numberOfServers);
        generateNRandomClients();  
    }

    //generate a number of numberOfClients clients with the ids taken in order and the arrivalTime and the processingTime random numbers in
    //the ranges [minArrivalTime, maxArrivalTime] and [minProcessingTime, maxProcessingTime]
    private void generateNRandomClients() {
        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = (int) ((Math.random() * (maxArrivalTime - minArrivalTime + 1)) + minArrivalTime);
            int processingTime = (int) ((Math.random() * (maxProcessingTime - minProcessingTime + 1)) + minProcessingTime);
            clients.add(new Client(i, arrivalTime, processingTime));
        }
        Collections.sort(clients);
    }
    
    private boolean areAllClientsProcessed() {
    	boolean flag = true;
    	for (Client c: clients) {
    		if (c.getProcessed() == false) {
    			flag = false;
    		}
    	}
    	return flag;
    }
 
    //@Override
    public void run() {
        try {
            int currentTime = 0;
            int totalWaitingTime = 0;
          
            writer = new FileWriter(output);
            while (currentTime <= timeLimit) {
            	
            	if (scheduler.areServersClosed() && areAllClientsProcessed()) {
            		break;
            	}
            	writer.write("Time: " + currentTime + "\n");
                for (Client c : clients) {
                	
                    if (c.getArrivalTime() == currentTime) {
                        scheduler.dispatchClient(c);
                        c.setProcessed(true);
                        totalWaitingTime += c.getWaitingTime();
                    }
                }
                writer.write("Waiting clients: " + "\n");
                for (Client c: clients) {
                	if (c.getProcessed() == false) {
                		writer.write(c + " ");
                	}
                }
                writer.write("\n" + scheduler.display() + "\n");
                currentTime++;
                Thread.sleep(1000);
            }
            
            scheduler.closeServers();
            
            writer.write("\n" + "Average waiting time: " + (float) totalWaitingTime/this.numberOfClients);
            writer.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) {
        SimulationManager gen = null;
		try {
			gen = new SimulationManager(args[0], args[1]);
			//gen = new SimulationManager("in-test-5.txt", "out-test-5.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Thread t = new Thread(gen);
        t.start();
    }
}
