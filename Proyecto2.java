import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Arrays;


public class Proyecto2 {

    // Number of tam_nodos
    public static int tam_nodos;
    public static int tam_aristas;
    public static int[][] Graph;
    public static int[][] Graph2;
    public static int [][] Graph3;
    public static float total = 0;

    static int Data_in (String archivo) //Lee el archivo que recibe y guarda los datos que contiene
    {
        Scanner file = null;
        try
        {
            file = new Scanner(new FileReader(archivo));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tam_nodos = file.nextInt();
        Graph = new int[tam_nodos][tam_nodos];
        Graph2 = new int[tam_nodos][tam_nodos];
        Graph3 = new int [tam_nodos][tam_nodos];
        tam_aristas = file.nextInt();

        int temp1=0;
        int temp2=0;
        int temp3=0;
        while(file.hasNext())
        {
            temp1 = file.nextInt();
            temp2 = file.nextInt();
            temp3 = file.nextInt();
            Graph[(temp1-1)][(temp2)-1] = temp3;
            Graph[(temp2-1)][(temp1-1)] = temp3;
            Graph2[(temp1-1)][(temp2)-1] = temp3;
            Graph3[(temp1-1)][(temp2)-1] = temp3;

        }
        /*for (int i=0;i<tam_nodos;i++)
        {
            System.out.println(Arrays.toString(Graph[i]));
        }*/


        return 1;
    }


    public static int min(int[] peso, boolean[] visitados) //Obtiene el camino con el menor peso disponible
    {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;
        for(int i = 0; i < tam_nodos; i++)
        {
            if(!visitados[i] && peso[i] <= min) {
                min = peso[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static void imprimirprim(int raiz[], int n, int[][] arbol) //Imprime el MST con el algoritmo de Prim
    {
        System.out.println("MST_Prim: ");
        System.out.println("Nodo_Inicial Nodo_Final Costo");

        for (int i = 1; i < tam_nodos; i++) {
            System.out.println(raiz[i]+1 + "        -        " + (i+1) + "       " + arbol[i][raiz[i]]);

        }
    }

    public static float prim(String archivo) //Llama al algoritmo de prim
    {
        Data_in(archivo);
        return prim2(Graph);
    }

    public static float prim2(int[][] arbol)
    {
        double startime = System.currentTimeMillis();
        int raiz[] = new int[tam_nodos];
        int peso[] = new int [tam_nodos];
        boolean visitados[] = new boolean[tam_nodos];
        for(int i = 0; i < tam_nodos; i++) //Pone los pesos como máximos y visitados a falso
        {
            peso[i] = Integer.MAX_VALUE;
            visitados[i] = false;
        }
        peso[0] = 0;
        raiz[0] = -1;
        for (int cont = 0; cont < tam_nodos - 1; cont++) //Busca las  aristas con menor peso que llevan a nodos no visitados
        {
            int temp = min(peso, visitados);
            visitados[temp] = true;
            for (int i = 0; i < tam_nodos; i++)
            {
                if (arbol[temp][i] != 0 && !visitados[i] && arbol[temp][i] <  peso[i]) // Si el nodo no ha sido visitado y su peso es menor al menor de los pesos
                {

                    raiz[i]  = temp;
                    peso[i] = arbol[temp][i];
                }
            }
        }

        for (int i=1;i<tam_nodos;i++) // Va sumando los el peso de las aristas utilizadas
        {
            total += arbol[i][raiz[i]];
        }

        imprimirprim(raiz, tam_nodos, arbol);
        double endtime = System.currentTimeMillis();
        double totaltime = (endtime - startime)/1000;
        System.out.println("Tiempo: " + totaltime);
        return total;
    }

    public static float Union_Find (int a, int b, int[] arr, int peso, float costo, boolean[] arr_vis) //Hace el algoritmo Union-Find y añade las aristas seleccionadas al costo del MST
    {
        int temp=0;

        if (b<a)
        {
            temp = a;
            a = b;
            b = temp;
        }
        if (arr[a] != arr[b]) //Si el padre del primer nodo es diferente al segundo
        {
            costo += peso; //Suma la arista actual
            System.out.println((a+1) + "        -        " + (b+1) + "       " + peso);
        }

        temp = arr[b];
        for (int i=0;i<arr.length;i++) //Cambia el padre del nodo más grande por el del más chico
        {
            if (arr[i] == temp)
            {
                arr[i] = a;
            }
            if (arr[i] == b)
            {
                arr[i] = a;
            }
        }

        if (arr[b] < b)// Vuelve a pasar por el arreglo para cambiar todos los nodos padres que faltaban
        {
            for (int i=0;i<arr.length;i++)
            {
                if (arr[i] == a)
                {
                    arr[i] = arr[a];
                }
            }
        }
        //System.out.println("Peso: " + peso);
        //System.out.println(Arrays.toString(arr));
        return costo;
    }

    public static boolean bool_check (boolean[] arr) //Checa que todos los nodos han sido visitados
    {
        for (int i=0;i<arr.length;i++)
        {
            if (arr[i] == false)
                return false;
        }
        return true;
    }

    public static float kruskalUF(String archivo)
    {
        Data_in(archivo);
        double startime = System.currentTimeMillis();
        int[] aristas_ord = new int[tam_aristas];
        int[] nodos = new int [tam_nodos];
        int[] nodos_padre = new int[tam_nodos];
        boolean[] visitados = new boolean[tam_nodos];
        boolean check_finish = false;
        float total = 0;
        int cont = 0;
        System.out.println("MST_Kruskal_UF: ");
        System.out.println("Nodo_Inicial Nodo_Final Costo");
        for (int i=0;i<tam_nodos;i++) //Ordena de mayor a menor las aristas y selecciona sus padres
        {
            nodos[i] = i;
            nodos_padre[i] = i;
            for (int j=0;j<tam_nodos;j++)
            {
                if (Graph2[i][j] != 0)
                {
                    aristas_ord[cont] = Graph2[i][j];
                    cont++;
                }
            }
        }
        Arrays.sort(aristas_ord);
        int current = 0;

        while (check_finish == false) //Crea el MST hasta que todos los nodos han sido visitados
        {
            for (int i = 0; i < tam_nodos; i++)
            {
                for (int j = 0; j < tam_nodos; j++)
                {
                    if (Graph2[i][j] == aristas_ord[current])
                    {
                        Graph2[i][j] = 0;
                        visitados[i] = true;
                        visitados[j] = true;
                        total = Union_Find(i,j, nodos_padre, aristas_ord[current], total, visitados);
                        check_finish = bool_check(visitados);
                    }
                }
            }

            current++;
        }

        double endtime = System.currentTimeMillis();
        double totaltime = (endtime - startime)/1000;
        System.out.println("Tiempo: " + totaltime);
        return total;
    }

    public static float DFS (int[] vis, int peso, float costo, int[] gray, boolean[] black) //Hace el algoritmo DFS para evitar que se formen ciclos
    {
        int temp=0;
        int a = gray[0];
        int b = gray[1];
        if (vis[a] != vis[b]) //En caso de que algún nodo no ha sido visitados
        {
            costo += peso;
            System.out.println((a+1) + "        -        " + (b+1) + "       " + peso);
        }
        temp = vis[b];
        for (int i=0;i<vis.length;i++) //Checa los hijos de cada nodo y verifica que no haya ciclos
        {
            if (vis[i] == temp)
            {
                vis[i] = a;
            }
            if (vis[i] == b)
            {
                vis[i] = a;
            }
        }
        if (vis[b] < b)
        {
            for (int i=0;i<vis.length;i++)
            {
                if (vis[i] == a)
                {
                    vis[i] = vis[a];
                    black[i] = true;
                }
            }
        }
        return costo;
    }

    public static float kruskalDFS (String archivo)
    {
        Data_in(archivo);
        double startime = System.currentTimeMillis();
        int[] aristas_ord = new int[tam_aristas];
        int[] white = new int [tam_nodos];
        int[] vis = new int[tam_nodos];
        boolean[] black = new boolean[tam_nodos];
        int[] gray = new int[tam_nodos];

        boolean check_finish = false;
        float total = 0;
        int cont = 0;
        System.out.println("MST_Kruskal_DFS: ");
        System.out.println("Nodo_Inicial Nodo_Final Costo");
        for (int i=0;i<tam_nodos;i++) //Ordena las aristas de mayor a menor
        {
            white[i] = i;
            vis[i] = i;
            for (int j=0;j<tam_nodos;j++)
            {
                if (Graph3[i][j] != 0)
                {
                    aristas_ord[cont] = Graph2[i][j];
                    cont++;
                }
            }
        }
        Arrays.sort(aristas_ord);
        int current = 0;

        while (check_finish == false)//Se hace el proceso hasta visitar todos los nodos
        {
            for (int i = 0; i < tam_nodos; i++)
            {
                for (int j = 0; j < tam_nodos; j++)
                {
                    if (Graph3[i][j] == aristas_ord[current])
                    {
                        gray[0]  = i;
                        gray[1] = j;
                        Graph3[i][j] = 0;
                        black[i] = true;
                        black[j] = true;
                        total = DFS(vis, aristas_ord[current], total, gray, black);
                        check_finish = bool_check(black);
                    }
                }
            }

            current++;
        }
        double endtime = System.currentTimeMillis();
        double totaltime = (endtime - startime)/1000;
        System.out.println("Tiempo: " + totaltime);
        return total;
    }

    public static void main(String[] args)
    {
        //System.out.println("Costo total (Prim): " + prim("P2Edges.txt"));
        //System.out.println("Costo total (Kruskal_U-F): " + kruskalUF("P2Edges.txt"));
        //System.out.println("Costo total (Kruskal_DFS): " + kruskalDFS("P2Edges.txt"));
    }
}