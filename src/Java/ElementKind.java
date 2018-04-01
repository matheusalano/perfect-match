public enum ElementKind {
    MAN(1), WOMAN(2), COUPLE(3), GROUND(4), WALL(5), REGISTRY_OFFICE(6);

    private final int value;

    ElementKind(int value) {
        this.value = value;
    }

    public int getValue () {
        return value;
    }

    public String getSymbol() {
        StringBuilder sb = new StringBuilder();
        switch (value) {
            case 1: sb.append("[Ma]"); break;
            case 2: sb.append("[Wo]"); break;
            case 3: sb.append("[Co]"); break;
            case 4: sb.append("----"); break;
            case 5: sb.append("####"); break;
            case 6: sb.append("[RO]"); break;
            default: sb.append("Err"); break; //error
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The curr element is: ");
        switch (value) {
            case 1: sb.append("Man"); break;
            case 2: sb.append("Woman"); break;
            case 3: sb.append("Couple"); break;
            case 4: sb.append("Ground"); break;
            case 5: sb.append("Wall"); break;
            case 6: sb.append("Registry Office"); break;
            default: sb.append("Undefined"); break; //error
        }
        sb.append("\n");
        return sb.toString();
    }
}