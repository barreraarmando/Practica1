/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CPractica1 {
    public static void main(String args[]){
        try{
            
            int pto = 3000;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            InetAddress host = null;
            String dir="";
            Scanner teclado = new Scanner(System.in);
            
            try{
                //System.out.println("Escribe la direccion del servidor:");
                dir = "localhost";
                host = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                main(args);
            }//catch
            Socket cl = new Socket(host,pto);
            System.out.println("Conexion con el servidor "+dir+":"+pto+" establecida\n");
            BufferedReader brRed = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta="archivos";
            String ruta_archivos = ruta+"\\"+carpeta+"\\";
            System.out.println("----------------------------Tu computadora--------------------------");
            System.out.println("ruta: "+ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            
            //Desplegar archivos cliente
            File[] paths;
            paths = f2.listFiles();
         
            for(File path:paths) {
               System.out.print(path.getName()+"---");
               if (path.isFile()){
                   System.out.println("Archivo");
               }
               if(path.isDirectory()){
                   System.out.println("Directorio");
               }
            }
            
            //Desplegar archivos del servidor
            System.out.println("");
            System.out.println("----------------------------Servidor--------------------------");
            String rutaServidor = brRed.readLine();
            int tamCarpeta = Integer.parseInt(brRed.readLine());
            System.out.println("ruta: "+rutaServidor);
            
            for(int i=1; i<=tamCarpeta; i++){
                System.out.print(brRed.readLine()+"---");
                System.out.println(brRed.readLine());
            }
            
            while(true){
                //Desplegar opciones
                System.out.println("");
                System.out.println("----------------------------Menu--------------------------");
                System.out.println("Escribe el numero de la opcion que deseas ejecutar: ");
                System.out.println("1. Subir archivo o carpeta");
                System.out.println("2. Descargar archivo o carpeta");
                System.out.println("3. Eliminar archivo o carpeta");
                String opcion = teclado.nextLine();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
