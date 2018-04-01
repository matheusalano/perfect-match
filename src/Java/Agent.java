public class Agent extends Element {
    private String name;
    private ElementKind sex;
    private Boolean single;
    private int[] matchPreference;
    private Agent partner;

    public Agent(String name, ElementKind sex, int[] matchPreference) {
        super(sex, name);
        this.matchPreference = matchPreference;
        this.single = true;
        partner = null;
    }

    public boolean isSingle() {
        return single;
    }

    public boolean isCouple() {
        return partner != null;
    }

    public void doMarriage(Agent partner) {
        this.single = false;
        this.partner = partner;
    }

    public void doDivorce() {
        this.single = true;
        this.partner = null;
    }
}