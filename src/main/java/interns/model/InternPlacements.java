package interns.model;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class InternPlacements {

    @PlanningEntityCollectionProperty
    private List<Intern> internList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "placementRange")
    private List<Placement> internPlacement;

    @PlanningScore
    private HardSoftScore score = null;
    
    public InternPlacements() {
    }

    public InternPlacements(List<Intern> internList, List<Placement> internPlacement) {
        this.internList = internList;
        this.internPlacement = internPlacement;
    }

    public List<Intern> getInternList() {
        return internList;
    }

    public void setInternList(List<Intern> internList) {
        this.internList = internList;
    }

    public List<Placement> getInternPlacement() {
        return internPlacement;
    }

    public void setInternPlacement(List<Placement> internPlacement) {
        this.internPlacement = internPlacement;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
