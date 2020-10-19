import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.*;

public class Estado {

static Paquetes paq;
static Transporte trans;
private ArrayList<Integer> env;// vector de todos los paquetes y en su interior el envio al que se asigan
private ArrayList<Double> cap; // capacidad de peso que tienen las ofertas de trasporte por dia
private int felicidad;
private double precio;
/*
    private ArrayList<Integer> paq_prioridad;
    private ArrayList<Double> peso_prioridad;
    private ArrayList<Integer> ofertas_dia;
    private ArrayList<Double> peso_dia;
    */

    //CONSTRUCTORA
    public Estado(int npaq, int seedpaq, int seedtrans, double prop)
    {
        paq = new Paquetes(npaq, seedpaq);
        trans = new Transporte(paq,prop, seedtrans);
        env = new ArrayList<Integer>(npaq);
        /*paq_prioridad = new ArrayList<Integer>();
        peso_prioridad = new ArrayList<Double>();
        for(int i = 0; i< 3; ++i)
        {
            paq_prioridad.add(0);
            peso_prioridad.add(0.0);
        }
        */

        for(int i = 0; i < npaq; ++i)
        {
            env.add(-1);
        }
        cap = new ArrayList<Double>(npaq);
        for (int i = 0; i < trans.size(); ++i)
        {
            cap.add(trans.get(i).getPesomax());
        }
        /*ofertas_dia = new ArrayList<Integer>();
        peso_dia = new ArrayList<Double>();
        for(int i = 0; i < 5; ++i)
        {
            ofertas_dia.add(0);
            peso_dia.add(0.0);
        }
        */
        felicidad = 0;
        precio = 0.0;
    }

    //METODOS

    public void  solIni1(){
        OrdenarPorPrioridad();
       // int nenv = 0;
        int dia  = 0;
        int nof = 0;
        //double penvio = 0;
    /*
        for(int i = 0; i < trans.size(); ++i)
        {
           int j =  trans.get(i).getDias() -1;
            peso_dia.set(j,peso_dia.get(j) + trans.get(i).getPesomax());
            ofertas_dia.set(j,ofertas_dia.get(j) + 1);
        }
    */
        for(int i = 0; i < env.size(); ++i)
        {
            /*penvio += paq.get(i).getPeso();
            if(penvio >= 50) {
                nenv++;
                penvio = paq.get(i).getPeso();
            }

             */
            if (CabePaquete(paq.get(i).getPeso(),nof)) {
                env.set(i,nof);
                cap.set(nof, cap.get(nof) - paq.get(i).getPeso() );
            }
            else {
                ++nof;
                dia = trans.get(nof).getDias()-1;
                //nenv ++;
                //penvio = 0.0;
                i--;
                int prior = paq.get(i).getPrioridad();
                felicidad -= CalculaFelicidad(dia,prior);
                precio -= CalculaPrecio(paq.get(i).getPeso(), trans.get(nof).getPrecio(), dia);
            }
            int prior = paq.get(i).getPrioridad();
            /*
            paq_prioridad.set(prior, paq_prioridad.get(prior) + 1 );
            peso_prioridad.set(prior, peso_prioridad.get(prior)+ paq.get(i).getPeso());
             */

            felicidad += CalculaFelicidad(dia,prior);
            precio += CalculaPrecio(paq.get(i).getPeso(), trans.get(nof).getPrecio(), dia);
        }
        escribeSolucion();
    }

    public void solIni2(){
        OrdenarPorPrioridad();

        int dia  = 0;
        int nof;

        for(int i = 0; i < env.size(); ++i)
        {
            nof = 0;
            while(!CabePaquete(paq.get(i).getPeso(),nof) )++nof;

            env.set(i,nof);
            cap.set(nof, cap.get(nof) - paq.get(i).getPeso() );

            int prior = paq.get(i).getPrioridad();
            dia = trans.get(nof).getDias()-1;
            felicidad  += CalculaFelicidad(dia,prior);
            precio += CalculaPrecio(paq.get(i).getPeso(), trans.get(nof).getPrecio(), dia);
        }
        escribeSolucion();

    }

    //FUNCIONES AUXILIARES

    public void OrdenarPorPrioridad()
    {
        //Mirar como usar punteros!!!

        Collections.sort(paq, new Comparator<Paquete>() {
            @Override
            public int compare(Paquete paquete1, Paquete paquete2) {
                return ((Integer) paquete1.getPrioridad()).compareTo((Integer) paquete2.getPrioridad());
            }
        });
    }

    public boolean CabePaquete(double peso, int nof)
    {
        return cap.get(nof) >= peso;
    }

    public int CalculaFelicidad(int dia, int prioridad){
        int fel = 0;
        if(prioridad == 1 && dia == 0){
            fel = 1;
        }
        else if(prioridad == 2 && dia == 0){
            fel = 3;
        }
        else if(prioridad == 2 && dia == 1){
            fel = 2;
        }
        else if(prioridad == 2 && dia == 2){
            fel = 1;
        }
        return fel;
    }

    public double CalculaPrecio(double peso, double oferta, int dia){
        double prec = 0.0;
            prec += oferta * peso;
            if(dia > 1){
                if(dia == 4) prec += 0.5 * peso;
                else prec += 0.25 * peso;
            }
            return prec;
    }

    //Escritora
    public void escribeSolucion()
    {
        System.out.println("ESTADO  \n");
        System.out.println("Envios: \n");
        for (int i = 0; i < env.size(); i++) {
            System.out.print("Paquete " + i + ": " + paq.get(i).getPeso() + "kg/PR" + paq.get(i).getPrioridad());
            System.out.print(" - Envio " + env.get(i) + "\n");
        }
        System.out.println("\nOfertas: \n");
        for (int i = 0; i < trans.size(); i++) {
            System.out.print("Oferta " + i + ": Dia " + trans.get(i).getDias() + " , " + cap.get(i) + "/" + trans.get(i).getPesomax() + "kg, " + trans.get(i).getPrecio() + " €/kg \n");
        }
        System.out.println("\nPRECIO: "+precio+"\n");
        System.out.println("FELICIDAD: "+felicidad+"\n");


/*
        System.out.println(" -------- Paquetes  ------------");
        for(int i = 0; i < env.size(); ++i)
        {
            System.out.println(paq.get(i).toString());
        }
        System.out.println();
        System.out.println();

        for(int i = 0; i < 3; ++i) {
            System.out.println("Prioridad " + i + " N paq=" + paq_prioridad.get(i) + " Peso total= " + peso_prioridad.get(i));
        }

        System.out.println();
        System.out.println(" -------- Ofertas  ------------");
        System.out.println("num ofertas = " + trans.size());
        System.out.println();

        for(int i = 0; i < trans.size(); ++i)
        {
            System.out.println(trans.get(i).toString());
        }

        System.out.println();
        System.out.println();
        for(int i = 0; i < 5; ++i) {
            System.out.println("Dia " + (i + 1) + " N ofertas=" + ofertas_dia.get(i) + " Peso maximo= " + peso_dia.get(i));
        }
*/
    }

}
