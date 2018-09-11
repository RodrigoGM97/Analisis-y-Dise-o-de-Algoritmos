

public class Mochila_SinRep {
    public static void main(String[] args)
    {
        //añade un valor más al peso y al num de elementos.
        int numelementos=5;
        int peso=9;
        int pesos[]={4,3,2,5};
        int valores[]={23,20,22,21};
        int arreglo[][] = new int [numelementos][peso];

        for(int i = 1;i<numelementos;i++) {
            for(int j = 1;j<peso;j++) {
                if(j>=pesos[i-1]) {
                    arreglo[i][j]= Math.max(arreglo[i-1][j],arreglo[i-1][j-pesos[i-1]]+valores[i-1]);
                }else {
                    arreglo[i][j]=arreglo[i-1][j];
                }

            }
        }

        for(int j = peso-1;j>=0;j--) {
            System.out.print(j+"=>\t");
            for(int i = 0;i<numelementos;i++) {
                System.out.print(arreglo[i][j]+"\t");
            }
            System.out.println();

        }
        System.out.print("\t");
        for(int i=0;i<numelementos;i++) {
            System.out.print("^\t");
        }
        System.out.println();
        System.out.print("\t");
        for(int i=0;i<numelementos;i++) {
            System.out.print(i+"\t");
        }
        System.out.println();
        System.out.print("pesos:\t");
        System.out.print(" \t");
        for(int i=0;i<numelementos-1;i++) {
            System.out.print(pesos[i]+"\t");
        }
        System.out.println();
        System.out.print("valores:\t");
        for(int i=0;i<numelementos-1;i++) {
            System.out.print(valores[i]+"\t");
        }
    }

}

