
import java.util.*;
public class Main {

    public static void main(String[] args)
    {
        System.out.println("Introduce número de paquetes:");
        Scanner sc = new Scanner(System.in);
        int npaq = sc.nextInt();
        System.out.println("Introduce la Seed:");
        int seed = sc.nextInt();

        Estado est = new Estado(npaq,seed, seed, 2.8);
        //est.solIni1();

        est.solIni2();
    }
}
