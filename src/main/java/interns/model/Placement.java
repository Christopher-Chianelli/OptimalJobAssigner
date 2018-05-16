package interns.model;

public class Placement {

    private String position;
    private Integer positionsAvaliable;
    
    public Placement() {
    }

    public Placement(String position, Integer positionsAvaliable) {
        this.position = position;
        this.positionsAvaliable = positionsAvaliable;
    }

    public String getPosition() {
        return position;
    }

    public Integer getPositionsAvaliable() {
        return positionsAvaliable;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }

    public void setPositionsAvaliable(Integer positionsAvaliable) {
        this.positionsAvaliable = positionsAvaliable;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Placement) {
            Placement placement = (Placement) other;
            return this.position.equals(placement.position);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

}
