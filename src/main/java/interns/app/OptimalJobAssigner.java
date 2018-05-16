package interns.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import interns.model.Intern;
import interns.model.InternPlacements;
import interns.model.Placement;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

public class OptimalJobAssigner {

    public static void main(String[] args) throws FileNotFoundException {
        SolverFactory<InternPlacements> solverFactory = SolverFactory.createFromXmlResource(
                "interns/app/solverConfig.xml");
        Solver<InternPlacements> solver = solverFactory.buildSolver();

        List<Placement> placements = getPlacements(args[0]);
        Map<String, Placement> placementMap = placements.stream().collect(Collectors.toMap(p -> p.getPosition(), p -> p));
        List<Intern> interns = getInterns(Arrays.asList(args).subList(1, args.length), placementMap);

        InternPlacements internPlacements = new InternPlacements(interns, placements);
        
        solver.addEventListener(event -> {
            if (event.isEveryProblemFactChangeProcessed()) {
                System.out.printf("New best solution found\n");
                printSolution(event.getNewBestSolution());
            }
        });
        
        System.out.printf("Finished loading data. Beginning solve.\n");
        internPlacements = solver.solve(internPlacements);
        System.out.printf("Solving finished.\n");
       
        printSolution(internPlacements);
    }
    
    private static void printSolution(InternPlacements internPlacements) {
        System.out.printf("Best score: Hard: %d, Soft: %d\n", internPlacements.getScore().getHardScore(), internPlacements.getScore().getSoftScore());
        System.out.printf("Placements:\n");

        for (Intern intern : internPlacements.getInternList()) {
            System.out.println(intern.getName());
            if (intern.getPlacement() != null) {
            System.out.printf("\t%s: %s (%d).\n", intern.getName(), intern.getPlacement().getPosition(), intern.getPlacementRanking());
            }
            else {
                System.out.printf("\t%s: is unassigned.\n", intern.getName());
            }
        }
    }
    
    private static List<Placement> getPlacements(String fileName) throws FileNotFoundException {
        List<Placement> placementList = new ArrayList<>();
        File file = new File(fileName);
        Scanner fileReader = new Scanner(file);
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            String[] parts = line.split(" ");
            switch (parts.length) {
                case 1:
                    placementList.add(new Placement(parts[0], 1));
                    break;
                    
                case 2:
                    placementList.add(new Placement(parts[1], Integer.parseInt(parts[0])));
                    break;
                    
                default:
                    fileReader.close();
                    throw new IllegalArgumentException("Bad line: " + line);
            }
        }
        fileReader.close();
        return placementList;
    }

    private static List<Intern> getInterns(List<String> internFileNames, Map<String, Placement> placementMap) throws FileNotFoundException {
        List<Intern> internList = new ArrayList<>();
        for (String fileName : internFileNames) {
            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);
            int lineNum = 0;
            String internName = null;
            List<Placement> internRankings = new ArrayList<>();
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                switch (lineNum) {
                    case 0:
                        internName = line;
                        break;
                        
                    default:
                        internRankings.add(placementMap.get(line));
                        break;
                }
                lineNum++;
            }
            internList.add(new Intern(internName, internRankings));
            fileReader.close();
        }
        return internList;
    }


}
