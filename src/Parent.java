public class Parent {
	
	private static String nodeName;
	private static int listeningPort;
	public static int child1;
	public static int child2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 4){
			System.out.println("Usage: java NewParent <listening port> <first child> <second child> <name>");
            return;
		}
		
		listeningPort = Integer.parseInt(args[0]);
		child1 = Integer.parseInt(args[1]);
		child2 = Integer.parseInt(args[2]);
		nodeName = args[3];//Assign 3rd argument to nodename

		InfectionClass infecter = new InfectionClass(listeningPort,child1,child2,nodeName);//Instantiate infection class
		infecter.start();//Start the input thread
		
	}
}
