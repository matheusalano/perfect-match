public class Element {
    private String named;
    private ElementKind kind;

    public Element(ElementKind kind, String named) {
        this.kind = kind;
        this.named = named;
    }

    public Element(ElementKind kind) {
        this.kind = kind;
        this.named = "";
    }

    public Element getElement() {
        return this;
    }

    public ElementKind getKind() {
        return kind;
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