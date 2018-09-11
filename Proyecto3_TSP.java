import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Proyecto3_TSP {
    public static float[][] Arr_Data_XY;
    public static float[][] Matrix;
    public static int node_size;
    public static int[] subset_nodos;
    public static int[][] subsets;
    public static List<String> x;
    public static HashMap<String, Float> TSP;
    public static String nodos = "";
    public static List<String> per_nodos;

    static void Data_in (String archivo) //Lee el archivo que recibe y guarda los datos que contiene
    {
        Scanner file = null;
        try
        {
            file = new Scanner(new FileReader(archivo));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        float temp1;
        float temp2;
        int i=0;
        node_size = file.nextInt();
        Arr_Data_XY = new float[node_size][2];
        subset_nodos = new int[node_size-1];
        while(file.hasNext())
        {
            temp1 = file.nextFloat();
            temp2 = file.nextFloat();
            Arr_Data_XY[i][0] = temp1;
            Arr_Data_XY[i][1] = temp2;

            if (i!=0)
                subset_nodos[i-1] = i;
            i++;

        }
        /*for (int j=0;j<node_size;j++)
        {
            System.out.println(Arrays.toString(Arr_Data_XY[j]));
        }*/
        for (int z=0;z<subset_nodos.length;z++)
        {
            nodos = nodos+subset_nodos[z];
        }
    }

    static void Dist_Puntos() //Calcula la distancia entre todos los puntos
    {
        float dist = 0;
        Matrix = new float[node_size][node_size];
        for (int i=0;i<node_size;i++)
        {
            for (int j=0;j<node_size;j++)
            {
                dist = (float) (Math.sqrt(Math.pow((Arr_Data_XY[i][0]-Arr_Data_XY[j][0]), 2)+ Math.pow((Arr_Data_XY[i][1]-Arr_Data_XY[j][1]),2)));
                Matrix[i][j] = dist;
            }
        }
        /*for (int j=0;j<node_size;j++)
        {
            System.out.println(Arrays.toString(Matrix[j]));
        }*/
    }

    static void Subsets(int set[]) //Genera las permutaciones de las ciudades
    {
        int n = set.length;
        subsets = new int[(int) Math.pow(2,subset_nodos.length)][(int) Math.pow(2,subset_nodos.length)];
        int cont = 0;
        String sub = "";
        // Run a loop for printing all 2^n
        // subsets one by one
        for (int i = 0; i < (1<<n); i++)
        {
            for (int j = 0; j < n; j++)
                if ((i & (1 << j)) > 0)
                {
                    sub = sub + set[j] + " ";
                    cont++;
                }
            sub = sub + ",";
        }

        x = Arrays.asList(sub.split("\\s*,\\s*"));
        for (int i = 0;i<x.size();i++)
        {
            int j = 0;
            StringTokenizer div = new StringTokenizer(x.get(i)," ");
            //System.out.println("x_i "+x.get(i));
            while (div.hasMoreTokens())
            {
                subsets[i][j] = Integer.parseInt(div.nextToken());
                j++;
            }

        }
        //print subset
        for (int i = 0;i<subsets.length;i++)
        {
            for (int j = 1;j<subsets[i].length;j++)
            {
                if (subsets[i][j] == 0)
                {
                    subsets[i][j] = -1;
                }
            }
            //System.out.println(Arrays.toString(subsets[i]));
        }
    }

    public static float TSP_dynamic() //Ejecuta el TSP con Dynamic Programming
    {
        double startime = System.currentTimeMillis();
        Dist_Puntos();
        Subsets(subset_nodos);
        TSP = new HashMap<String, Float>();
        float min_last = 0;
        float temp = 0;
        int cont = 0;
        for (int i=0;i<node_size-1;i++)
        {
            for (int j=0;j<x.size()-1;j++)
            {
                TSP.put(Integer.toString(i+1)+","+x.get(j),temp);
            }
        }

        //Llena los datos del inicio a cada nodo
        for (int i=0;i<node_size-1;i++)
        {
            TSP.put(Integer.toString(i+1)+",",Matrix[0][i+1]);
        }
        //System.out.println(TSP);
        for (int j=1;j<x.size()-1;j++) //Recorre las permutaciones y genera el costo de cada permutacion
        {
            for (int i=1;i<node_size;i++)
            {
                StringTokenizer div = new StringTokenizer(x.get(j)," ");
                ArrayList<Float> min_arr = new ArrayList<Float>();
                ArrayList<Integer> min_arr_pos = new ArrayList<Integer>();
                while (div.hasMoreTokens())
                {
                    String t = div.nextToken();
                    min_arr.add((float) Matrix[Integer.parseInt(t)][i]);
                    min_arr_pos.add(Integer.parseInt(t));
                }
                min_last = check_cam(min_arr,min_arr_pos, i, j);
                cont  += 1;
            }
        }
        StringTokenizer div = new StringTokenizer(x.get(x.size()-1)," ");
        ArrayList<Float> min_arr = new ArrayList<Float>();
        ArrayList<Integer> min_arr_pos = new ArrayList<Integer>();
        while (div.hasMoreTokens())
        {
            String t = div.nextToken();
            min_arr.add((float) Matrix[Integer.parseInt(t)][0]);
            min_arr_pos.add(Integer.parseInt(t));
        }
        min_last = check_cam(min_arr,min_arr_pos, 0, x.size()-1);
        //System.out.println("Costo Final: "+ min_last);
        double endtime = System.currentTimeMillis();
        double totaltime = (endtime - startime)/1000;
        System.out.println("Tiempo: " + totaltime);
        return min_last;
    }

    public static float check_cam(ArrayList dist_p_obj, ArrayList obj_pos, int obj, int b) //Funcion para determinar el camino a evaluar en Dynamic Programming
    {
        List<String> pass_through = new ArrayList<>(x);
        //System.out.println("Camino p: " + dist_p_obj.toString());
        //System.out.println("obj: " + obj + " por: " + pass_through.get(b));
        String way_check[] = new String[obj_pos.size()];
        String path_actual = obj+","+pass_through.get(b);
        //System.out.println(TSP);
        for (int i = 0; i<obj_pos.size();i++)
        {
            StringTokenizer sb = new StringTokenizer(pass_through.get(b), obj_pos.get(i).toString());
            way_check[i] = obj_pos.get(i).toString() + ",";
            while (sb.hasMoreTokens())
            {
                way_check[i] += sb.nextToken();
            }
            way_check[i] = way_check[i].replaceAll(", ",",");
            way_check[i] = way_check[i].replaceAll("  "," ");
            way_check[i] = way_check[i].trim();
            //System.out.println("waycheck: "+way_check[i]);
        }

        return check_min(way_check,dist_p_obj, path_actual);
    }

    public static float check_min (String[] way_check,ArrayList dist_p_obj, String path_actual) //Verifica el camino más corto en Dynamic Programming
    {
        float[] min = new float[dist_p_obj.size()];
        float min_cost = 0;
        for (int i = 0; i<dist_p_obj.size();i++)
        {
            //System.out.println(TSP.get(way_check[i]));
            min[i] = (float) dist_p_obj.get(i) + TSP.get(way_check[i]);

        }
        Arrays.sort(min);
        min_cost = min[0];
        //System.out.println("");
        //System.out.println("Path:" + path_actual);
        TSP.put(path_actual, min_cost);
        //System.out.println("Costo: " + min_cost);
        //System.out.println("");
        return min_cost;
    }

    public static float TSP_Brute()
    {
        double startime = System.currentTimeMillis();
        Dist_Puntos();
        Subsets(subset_nodos);
        per_nodos = new ArrayList<String>();
        float[] costo_FB = new float[per_nodos.size()];
        permutation(nodos);
        String temp;
        String camino = "";
        float costo_f = 999999999;

        for (int i=0;i<per_nodos.size()-1;i++)
        {
            temp = per_nodos.get(i);
            float cost_t = Matrix[0][Character.getNumericValue(temp.charAt(0))];
            //System.out.println(per_nodos.get(i));
            for (int j=0;j<temp.length()-1;j++)
            {
                cost_t += Matrix[Character.getNumericValue(temp.charAt(j))][Character.getNumericValue(temp.charAt(j+1))];
            }
            cost_t += Matrix[Character.getNumericValue(temp.charAt(temp.length()-1))][0];
            if (cost_t<costo_f)
            {
                camino = temp;
                costo_f = cost_t;
            }

        }
        //System.out.println("costo "+ costo_f);
        double endtime = System.currentTimeMillis();
        double totaltime = (endtime - startime)/1000;
        System.out.println("Ciudades visitadas: "+ camino);
        System.out.println("Tiempo: " + totaltime);
        return  costo_f;
    }
    //Genera las combinaciones para Brute Force de caminos
    private static void permutation(String string)
    {
        printPermutation(string,"");
    }
    private static void printPermutation(String string, String permutation) {

        if(string.length()==0){
            //System.out.println(permutation);
            per_nodos.add(permutation);
            return;
        }

        for (int i = 0; i < string.length(); i++) {
            char toAppendToPermutation = string.charAt(i);
            String remaining = string.substring(0, i) + string.substring(i + 1);

            printPermutation( remaining,  permutation + toAppendToPermutation);
        }
    }
    public static void main(String[] args)
    {
        Data_in("P4tsp25.txt");
        System.out.println("Costo Final (TSP-DynamicProgramming): " + TSP_dynamic());
        //System.out.println("Costo Final (TSP-BruteForce): " + TSP_Brute());
    }
}
