
public class Element {
    private String named;
    private ElementKind kind;
    private Position position;

    public Element(ElementKind kind, Position position, String named) {
        this.kind = kind;
        this.position = position;
        this.named = named;
    }

    public Element(ElementKind kind, Position position) {
        this.kind = kind;
        this.position = position;
        this.named = "";
    }

    public Element getElement() {
        return this;
    }

    public ElementKind getKind() {
        return kind;
    }

    public Position getPosition() { return position; }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getSymbol() {
        StringBuilder sb = new StringBuilder();
        if (named.isEmpty())
            sb.append(kind.getSymbol());
        else
            sb.append(named);
        return sb.toString();
    }

    @Override public String toString() {
        return kind.toString();
    }
}