package interns.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Intern {

    private String name;

    @PlanningPin
    private boolean pinnedByUser = false;

    @PlanningVariable(valueRangeProviderRefs = "placementRange")
    private Placement placement = null;

    private Map<Placement, Integer> placementRankings;
    
    public Intern() {
    }

    public Intern(String name, Map<Placement, Integer> placementRankings) {
        this.name = name;
        this.placementRankings = placementRankings;
    }

    public Intern(String name, List<Placement> placementRankingList) {
        this.name = name;
        this.placementRankings = new HashMap<>();
        for (int i = 0; i < placementRankingList.size(); i++) {
            placementRankings.put(placementRankingList.get(i), -(i * i));
        }
    }

    public int getPlacementListRanking() {
        if (placement == null) {
            throw new IllegalStateException("placement is null!");
        }
        return (int) Math.round(Math.sqrt(-placementRankings.get(placement)));
    }
    
    public int getPlacementRanking() {
        return placementRankings.getOrDefault(placement, 0);
    }

    public String getName() {
        return name;
    }

    public Placement getPlacement() {
        return placement;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }
    
    public Map<Placement, Integer> getPlacementRankings() {
        return placementRankings;
    }

    public void setPlacementRankings(Map<Placement, Integer> placementRankings) {
        this.placementRankings = placementRankings;
    }

    public boolean isPinnedByUser() {
        return pinnedByUser;
    }

    public void setPinnedByUser(boolean isPinned) {
        pinnedByUser = isPinned;
    }

}
