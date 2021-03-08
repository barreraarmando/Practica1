/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CPractica1 {
    
    public static void enviarArchivo(File archivo, Socket cl){
        try{
            String path = archivo.getAbsolutePath();
            String nombre = archivo.getName();
            long tam = archivo.length();
            System.out.println("Preparandose pare enviar archivo "+path+" de "+tam+" bytes\n");
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(new FileInputStream(path));
            dos.writeUTF(nombre);
            dos.flush();
            dos.writeLong(tam);
            dos.flush();
            long enviados = 0;
            int l=0,porcentaje=0;
            while(enviados<tam){
                byte[] b = new byte[1500];
                l=dis.read(b);
                System.out.println("enviados: "+l);
                dos.write(b,0,l);
                dos.flush();
                enviados = enviados + l;
                porcentaje = (int)((enviados*100)/tam);
                System.out.print("\rEnviado el "+porcentaje+" % del archivo");
            }//while
            System.out.println("\nArchivo enviado..");
            dis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void desplegarArchivos(BufferedReader brRed, PrintWriter pwRed, File f2, String ruta_archivos){
        try{
            
            System.out.println("----------------------------Tu computadora--------------------------");
            System.out.println("ruta: "+ruta_archivos);

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
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /*
    public static void enviarZip(String nombreDir){
        try{
            FileOutputStream fos = new FileOutputStream("dirCompressed.zip");
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            File fileToZip = new File(nombreDir);

            zipFile(fileToZip, fileToZip.getName(), zipOut);
            zipOut.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
    */
    public static void enviarCarpeta(File fSubir, Socket cl, PrintWriter pw) throws InterruptedException{
        File[] hijos = fSubir.listFiles();
        int cantArchivosCarpeta = hijos.length;
        pw.println(cantArchivosCarpeta);
        pw.flush();
        for (File hijo : hijos) {
            enviarArchivo(hijo, cl);
            Thread.sleep(500);
        }
    }
    
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
            PrintWriter pwRed = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            File f = new File("");
            String ruta = f.getAbsolutePath();
            String carpeta="archivos";
            String ruta_archivos = ruta+"\\"+carpeta+"\\";
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            
            desplegarArchivos(brRed, pwRed, f2, ruta_archivos);
            
            while(true){
                //Desplegar opciones
                System.out.println("");
                System.out.println("----------------------------Menu--------------------------");
                System.out.println("Escribe el numero de la opcion que deseas ejecutar: ");
                System.out.println("0. Desplegar los archivos del equipo y del servidor");
                System.out.println("1. Subir archivo o carpeta");
                System.out.println("2. Descargar archivo o carpeta");
                System.out.println("3. Eliminar archivo o carpeta");
                System.out.println("4. Salir");
                System.out.print("Opcion: ");
                String opcionS = teclado.nextLine();
                System.out.println("");
                int opcion = Integer.parseInt(opcionS);
                pwRed.println(opcion);
                pwRed.flush();
                switch(opcion){
                    case(0):
                        desplegarArchivos(brRed, pwRed, f2, ruta_archivos);
                        break;
                    case(1):
                        System.out.println("Ingresa el nombre y la extensión del archivo o carpeta que quieres subir");
                        String entrada = teclado.nextLine();
                        String nombreSubir = ruta+"\\"+carpeta+"\\"+entrada;
                        File fSubir = new File(nombreSubir);
                        if (fSubir.exists() == false){
                            System.out.println("El archivo o carpeta que ingresaste no existe");
                            break;
                        }else{
                            if (fSubir.isFile()){
                                pwRed.println("archivo");
                                pwRed.flush();
                                enviarArchivo(fSubir, cl);
                            }else{
                                if (fSubir.isDirectory()){
                                    pwRed.println("carpeta");
                                    pwRed.flush();
                                    pwRed.println(entrada);
                                    pwRed.flush();
                                    enviarCarpeta(fSubir, cl, pwRed);
                                }
                            }
                        }
                        break;
                    case(2):
                        System.out.println("Caso para descargar archivo");
                        break;
                    case(3):
                        System.out.println("Caso para eliminar archivo");
                        break;  
                    case(4):
                        break; 
                    default:
                        System.out.println("Ingresa una opcion correcta");
                        break;
                }
                if (opcion == 4){
                    break;
                }
            }
            pwRed.close();
            brRed.close();
            br.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
