public class App {

    public static void main(String [] args) {
        String [][] mx = {{"[Ma]","----","----","----","----","----","----","[RO]"},
                          {"----","----","----","----","----","----","----","----"},
                          {"----","####","----","####","----","####","----","----"},
                          {"----","####","----","####","----","####","----","----"},
                          {"----","####","----","####","----","####","----","----"},
                          {"----","####","----","####","----","####","----","----"},
                          {"----","----","----","----","----","----","----","----"},
                          {"----","----","----","----","----","----","----","[Wo]"}};  
        Matrix matrix = new Matrix(mx);
        matrix.print();
	}
}