package asg2;


public class Client implements Comparable<Client>{

    private int id;
    private int arrivalTime;
    private int processingTime;
    private int waitingTime;
    private boolean processed; //true if the client was added to a queue

    public Client(int id, int arrivalTime, int processingTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.waitingTime = 0; //initial condition
        this.processed = false; //initial condition
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String toString() {
        String s = "(" + this.id + "," + this.arrivalTime + "," + this.processingTime + ")";
        return s;
    }

    //@Override
    public int compareTo(Client o) {
        return this.arrivalTime - o.getArrivalTime();
    }

}