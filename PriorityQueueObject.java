package csi403;

import java.io.Serializable;

/*
*Object made to be used in a PriorityQueue which has a command of either
*"enqueue" or "dequeue" to add to queue or remove from queue, a job name, and a priority pri
*if the command is dequeue then name and pri don't matter
*/
public class PriorityQueueObject implements Comparable<PriorityQueueObject>, Serializable{

	private String cmd;
	private String name;
	private int pri;
	
	//Constructor only sets value of cmd incase the command is dequeue
	//Sets default pri to -1 as -1 is not an accepted value (default of 0 would be accepted)
	public PriorityQueueObject(String cmd) {
		this.cmd = cmd;
		pri = -1;
	}
	
	//Accessor for cmd
	public String getCmd() {
		return cmd;
	}
	
	//Mutator for cmd
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	//Accessor for name
	public String getName() {
		return name;
	}
	
	//Mutator for name
	public void setName(String name) {
		this.name = name;
	}
	
	//Accessor for pri
	public int getPri() {
		return pri;
	}
	
	//Mutator for pri
	public void setPri(int pri) {
		this.pri = pri;
	}
	
	//Tests if cmd is enqueue and a name and pri >= 0 is present in the instance
	public boolean testEnqueue() {
		if(cmd != null && name !=null) {
			if(cmd.equals("enqueue") && pri > -1)
				return true;
		}
		return false;
	}
	
	//Tests if cmd is dequeue
	public boolean testDequeue() {
		if(cmd != null) {
			if(cmd.equals("dequeue"))
				return true;
		}
		return false;
	}
	
	//toString just returns the name field
	@Override
	public String toString() {
		return this.name;
	}
	
	//Compare PriorityQueueObjects based on pri
	@Override
	public int compareTo(PriorityQueueObject other) {
		int otherPri = other.getPri();
		if(pri > otherPri) {
			return 1;
		} else if(pri < otherPri) {
			return -1;
		} else {
			return 0;
		}
	}
}
