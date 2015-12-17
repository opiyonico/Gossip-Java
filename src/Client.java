public final class Client {
	
	private static int parent;
	private static int child1;
	private static int child2;
	private static String nodeName;
	public static int listeningPort;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 5){
			System.out.println("Usage: java NewClient <listening port> <parent> <first child> <second child> <name>");
            return;
		}
		
		listeningPort = Integer.parseInt(args[0]);
		parent = Integer.parseInt(args[1]);
		child1 = Integer.parseInt(args[2]);
		child2 = Integer.parseInt(args[3]);
		nodeName = args[4];
		
		InfectionClass infecter = new InfectionClass(listeningPort,parent,child1,child2,nodeName);
		infecter.start();
		
	}
		
}


