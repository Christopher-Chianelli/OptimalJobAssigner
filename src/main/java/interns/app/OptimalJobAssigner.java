package interns.app;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

        Scanner input = new Scanner(System.in);
        List<Placement> placements = getPlacements(input);
        List<Intern> interns = getInterns(input, placements);
        input.close();

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
            if (intern.getPlacement() != null) {
                System.out.printf("\t%s: %s (%d).\n", intern.getName(), intern.getPlacement().getPosition(), intern.getPlacementRanking());
            } else {
                System.out.printf("\t%s: is unassigned.\n", intern.getName());
            }
        }
    }

    private static List<Placement> getPlacements(Scanner fileReader) throws FileNotFoundException {
        List<Placement> placementList = new ArrayList<>();

        System.out.printf("Enter the placements, in the format [POSITIONS AVALIABLE] [NAME]. Enter a blank line to end the list.\n");
        System.out.flush();
        while (true) {
            String line = fileReader.nextLine();
            if (line.isEmpty()) {
                break;
            }
            int positionsAvaliable = Integer.parseInt(line.substring(0, line.indexOf(' ')));
            String positionName = line.substring(line.indexOf(' ') + 1);
            placementList.add(new Placement(positionName, positionsAvaliable));
        }

        return placementList;
    }

    private static List<Intern> getInterns(Scanner fileReader, List<Placement> placementList) throws FileNotFoundException {
        List<Intern> internList = new ArrayList<>();
        while (true) {
            System.out.printf("Intern name (blank to exit):\n");
            System.out.flush();
            String internName = fileReader.nextLine();

            if (internName.isEmpty()) {
                break;
            }
            List<List<Placement>> internRankings = new ArrayList<>();
            placementList.forEach((p) -> internRankings.add(new ArrayList<>()));

            for (Placement placement : placementList) {
                System.out.printf("%s Ranking for %s:\n", internName, placement.getPosition());
                int rank = fileReader.nextInt();
                internRankings.get(rank - 1).add(placement);
            }
            fileReader.nextLine();

            Map<Placement, Integer> internScoreRankings = new HashMap<>();
            for (int i = 0; i < internRankings.size(); i++) {
                final int SCORE = -(i * i);
                internRankings.get(i).forEach(p -> internScoreRankings.put(p, SCORE));
            }

            internList.add(new Intern(internName, internScoreRankings));
        }
        return internList;
    }

}
