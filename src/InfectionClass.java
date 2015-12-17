import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;


public class InfectionClass extends Thread{
	
	private String infectionToSpread;
	private int parent;
	private int child1;
	private int child2;
	public ArrayList<String> myInfections = new ArrayList<String>();
	public ArrayList<String> gossipSpreaded = new ArrayList<String>();
	public static ArrayList<Integer> susceptibleNodes = new ArrayList<Integer>();
	public static ArrayList<Integer> infectedNodes = new ArrayList<Integer>();
	public String latestGossip;
	private String nodeName;
	private Random randomGenerator = new Random();
	private int listeningPort;
	private static String gossipToSpread;
	private static boolean sender;
	
	//Child constructor
	public InfectionClass(int listeningPort,int parent,int child1,int child2,String nodeName){
		this.parent = parent;
		this.child1 = child1;
		this.child2 = child2;
		this.nodeName = nodeName;
		this.listeningPort = listeningPort;
		susceptibleNodes.add(this.parent);
		susceptibleNodes.add(this.child1);
		susceptibleNodes.add(this.child2);
	}
	//Parent constructor
	public InfectionClass(int listeningPort,int child1,int child2,String nodeName){
		this.parent = 0;
		this.child1 = child1;
		this.child2 = child2;
		this.nodeName = nodeName;
		this.listeningPort = listeningPort;
		susceptibleNodes.add(this.child1);
		susceptibleNodes.add(this.child2);
	}
	
	
	
	
	//Function to check if a node is has a particular gossip
	public boolean checkIfInfected(String infection){
		for (String infectionsFound : myInfections) {
            if(infectionsFound.equals(infection)){
            	return true;
            }
        }
		return false;
	}
	
	
	//Function to spread gossip
	public void spreadGossip(String gossip,ArrayList<Integer> susceptibleNodes){
		try{
			int chosenNode;
			ArrayList<Integer> nodesToSendTo = new ArrayList<Integer>(susceptibleNodes);
			//Check if the gossip has been spread before
			if(!gossipSpreaded.contains(gossip.substring(2))){
				gossipSpreaded.add(gossip.substring(2));//Add this to the list of spreaded gossip
				while(nodesToSendTo.size() != 0){
					
					chosenNode = randomGenerator.nextInt(nodesToSendTo.size());
					System.out.println("The node that has been chosen is node: " + nodesToSendTo.get(chosenNode) + "\n");
					packetSender(gossip,nodesToSendTo.get(chosenNode));
					nodesToSendTo.remove(chosenNode);
					System.out.println("The remaining nodes are: \n");
					for(Integer remainingNode : nodesToSendTo){
						System.out.println(remainingNode + "\n");
					}
					Thread.sleep(3000);
				}
				
				
			}
						
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	public void packetSender(String packetData,int port) throws IOException{
		DatagramSocket socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("127.0.0.1");//Localhost Address
		DatagramPacket packet = new DatagramPacket(packetData.getBytes(),packetData.length(),IPAddress,port);//Create packet
		socket.send(packet);//Send the packet
		socket.close();
	}
	
	public void receivingThread(){
		while(true){
			try{
				
				DatagramSocket socket = new DatagramSocket(listeningPort);
				
				byte[] buffer = new byte[100];
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length);//Create a packet to receive data
				
				socket.receive(packet);//Receive the packet
				
				
				gossipToSpread = new String(buffer);//Put the data received in the packet in the string
				
				
				
				String sendingNode = gossipToSpread.substring(0,2);//Get the sending Node
				String packetMessage = gossipToSpread.substring(2);//Get the message in the packet
				
				//Check if sending Node is this node
				if(sendingNode.equals(nodeName)){
					sender = true;//If true, make sender true
				}else{
					sender = false;//If false, make sender false
				}
				
				//If the sender is not this node, print out that the node has been infected
				if(!sender){
					if(!myInfections.contains(packetMessage)){
						System.out.println("Node " + nodeName + " has been infected with: " + packetMessage + "by Node " + sendingNode);
						myInfections.add(packetMessage);
					}
				}else if(sender){
					if(!myInfections.contains(packetMessage)){
						myInfections.add(packetMessage);
					}
				}else{
					System.out.println("I've already been infected\n");
				}
				
				//System.out.println("I'm in the gossip to spread method.");
				
				int chosenNode;
				ArrayList<Integer> nodesToSendTo = new ArrayList<Integer>(susceptibleNodes);
				
				
				
				if(!gossipSpreaded.contains(packetMessage)){
					gossipSpreaded.add(packetMessage);
					
					while(nodesToSendTo.size() != 0){
						
						chosenNode = randomGenerator.nextInt(nodesToSendTo.size());
						System.out.println("The node that has been chosen is node: " + nodesToSendTo.get(chosenNode)+ "\n");
						packetSender(gossipToSpread,nodesToSendTo.get(chosenNode));
						nodesToSendTo.remove(chosenNode);
						//System.out.println("The remaining nodes are: \n");
//						for(Integer remainingNode : nodesToSendTo){
//							System.out.println(remainingNode + "\n");
//						}
						Thread.sleep(2000);
					}
					
					System.out.println("Gossip has been spread :-)");
				}
				
				socket.close();
				
			}catch(Exception e){e.printStackTrace();}
		}
	}
	
	public void inputThread(){
		while(true){
			System.out.println("Please write a message to send: ");
			@SuppressWarnings("resource")
			Scanner userInput = new Scanner(System.in);
			infectionToSpread = nodeName + userInput.nextLine() + "\n";//Add nodename to message
			myInfections.add(infectionToSpread.substring(2));
			spreadGossip(infectionToSpread,susceptibleNodes);
			
			//Sleep thread for 2 seconds
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//Input Thread
	public void run(){
		
		new Thread(new Runnable() {
            public void run() {
                receivingThread();
            }
        }).start();
		
		new Thread(new Runnable() {
            public void run() {
               inputThread();
            }
        }).start();
		
	}
}


