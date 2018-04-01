public class Matrix {
    private Element[][] matrix;
    
    public Matrix(String [][] matrix) {
        this.matrix = new Element[matrix.length][matrix[0].length];
        for(int l=0; l < matrix.length; l++) 
            for(int c=0; c < matrix[l].length; c++) {
                if (matrix[l][c].equals(ElementKind.GROUND.getSymbol())) this.matrix[l][c] = new Element(ElementKind.GROUND);
                if (matrix[l][c].equals(ElementKind.WALL.getSymbol())) this.matrix[l][c] = new Element(ElementKind.WALL);
                if (matrix[l][c].equals(ElementKind.MAN.getSymbol())) this.matrix[l][c] = new Element(ElementKind.MAN);
                if (matrix[l][c].equals(ElementKind.WOMAN.getSymbol())) this.matrix[l][c] = new Element(ElementKind.WOMAN);
                if (matrix[l][c].equals(ElementKind.COUPLE.getSymbol())) this.matrix[l][c] = new Element(ElementKind.COUPLE);
                if (matrix[l][c].equals(ElementKind.REGISTRY_OFFICE.getSymbol())) this.matrix[l][c] = new Element(ElementKind.REGISTRY_OFFICE);
            }
    } 

    public Matrix(Element[][] matrix) {
        this.matrix = matrix;
    }

    public ElementKind getElementKind(int x, int y) {
        return matrix[x][y].getKind();
    }
    
    public String whatIs(int x, int y) {
        return matrix[x][y].toString();
    }

    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (int l=0; l < matrix.length; l++) {
            for (int c=0; c < matrix[l].length; c++) {
                sb.append(matrix [l][c].getSymbol());
                sb.append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.print(sb.toString());
    }
}