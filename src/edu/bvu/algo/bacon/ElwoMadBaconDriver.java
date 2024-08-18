package edu.bvu.algo.bacon;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.bvu.algo.AdjListGraph;
import edu.bvu.algo.Graph;
import edu.bvu.algo.GraphTuple;

public class ElwoMadBaconDriver {
	
	// member variables can go here
	
	// movieMap = <movieTitle, List<actors>>
	Map<String, List<String>> movieMap = new HashMap<String, List<String>>();
	
	// edgeMap = <actorsKey, movie> (for both directions)
	Map<String, String> edgeMap = new HashMap<String, String>();
	
	// actorGraph = Graph
	Graph actorGraph = new AdjListGraph();
	
	public void run(String dbFile) {
		
		//File movieFile = new File(dbFile);
		
		// Fill movieMap 
		fillMovieMap(dbFile);
		
		// Build the graph
		buildGraph();
				
		// BFS the graph
		Map<String, GraphTuple> bfsResults = actorGraph.bfs("Kevin Bacon");
		
		// Get input from user for actorName
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome to the Oracle of Bacon!");
		System.out.print("Enter actor/actress name: ");
		String actorName = scanner.nextLine();
		
		actorName = actorName.trim();
		
		while (true) {
		
			// check what the input is
			if (actorName.equals("Kevin Bacon")) {
				System.out.println("\nKevin Bacon has a Bacon number of 0. He IS Kevin Bacon!\n");
			}
			
			else if (actorName.isBlank()) {
				System.out.println("\nThank you for using the Oracle of Bacon.");	
				return;
			}
		
			else {
				if (!actorGraph.containsNode(actorName)) {
					System.out.println("\n" + actorName + " has a Bacon number of infinity!\n");
				}
			
				else {
				findBaconNum(actorName, bfsResults);
				}
			}
		
			System.out.print("Enter actor/actress name: ");
			actorName = scanner.nextLine();
			actorName = actorName.trim();
		}
	}
	
	
	private void fillMovieMap(String movieFile) {
		List<String> lines = new ArrayList<String>();
		
		try {
			Path path = Paths.get(movieFile);
			lines = Files.readAllLines(path);
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String movieTitle = "";
		for (String line : lines) {
			
			if (line.startsWith("M:")) {
				movieTitle = line.substring(3);
				ArrayList<String> currMovieActors = new ArrayList<String>();
				movieMap.put(movieTitle, currMovieActors);
			}
			
			else if (line.isBlank()) {
				
			}
			
			else {
				movieMap.get(movieTitle).add(line);
			}
		}
	}
	
	
	private void buildGraph() {
		// nodes = actors
		// edges = movies
		
		for (Map.Entry<String, List<String>> movie : movieMap.entrySet()) {
			String movieTitle = movie.getKey();
			for (String actor : movie.getValue()) {
				for (String otherActor : movie.getValue()) {
					if (otherActor.equals(actor)) {
						
					}
					else {
						actorGraph.addEdge(actor, otherActor);
						String key1 = actor + "___" + otherActor;
						String key2 = otherActor + "___" + actor;
						edgeMap.put(key1, movieTitle);
						edgeMap.put(key2, movieTitle);
					}
				}
			}
		}
	}
	
	
	private void findBaconNum(String actorName, Map<String, GraphTuple> bfsResults) {
		if (bfsResults.get(actorName).d == null) {
			System.out.println("\n" + actorName + " has a Bacon number of infinity!\n");
			return;
		}
		int baconNumber = bfsResults.get(actorName).d;
		
		System.out.println("\n" + actorName + " has a Bacon number of " + baconNumber + ".");
		String currActor = actorName;
		String returnString = "";
		while (!currActor.equals("Kevin Bacon")) {
			String parent = bfsResults.get(currActor).p;
			returnString = returnString + "    " + currActor + " was in \"" 
							+ edgeMap.get(currActor + "___" + parent) 
							+ "\" with " + parent + ".\n";
			
			currActor = parent;
		}
		System.out.println(returnString);
	}
	
	
	public static void main(String[] args) {
		ElwoMadBaconDriver e = new ElwoMadBaconDriver();
		e.run("Movies.txt");

	}

}
