package com.proyecto.proyectobigdatapt1;

import com.proyecto.proyectobigdatapt1.servicios.CargarDatos;
import com.proyecto.proyectobigdatapt1.servicios.extraerData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.IOException;

public class ProyectoBigDataPt1 {

    public static void main(String[] args) {
        // Validar longitud mínima de argumentos
        System.setProperty("hadoop.home.dir", "/opt/empty-dir");
        
        if (args.length < 2) {
            System.out.println("Uso: java -jar ProyectoBigDataPt1-1.0-SNAPSHOT.jar <comando> <arg1> <arg2> <arg3>");
            return;
        }

        try {
            // Configuración de HDFS
            Configuration configuration = new Configuration();
            configuration.addResource("/usr/local/hadoop/etc/hadoop/core-site.xml");
            configuration.addResource("/usr/local/hadoop/etc/hadoop/hdfs-site.xml");

            // Obtener el sistema de archivos de HDFS
            FileSystem fileSystem = FileSystem.get(configuration);

            // Crear un nuevo directorio en HDFS
            Path hdfsPath = new Path("/user/hadoop/testdir");
            if (!fileSystem.exists(hdfsPath)) {
                fileSystem.mkdirs(hdfsPath);
                System.out.println("Directorio creado en HDFS");
            } else {
                System.out.println("El directorio ya existe");
            }

            // Cerrar el sistema de archivos
            fileSystem.close();

            // Comando principal
            String command = args[0];

            switch (command.toLowerCase()) {
                case "cargar":
                    if (args.length != 4) {
                        System.out.println("Uso para cargar: java -jar ProyectoBigDataPt1-1.0-SNAPSHOT.jar cargar <dataset.csv> <F> <C>");
                        return;
                    }
                    String csvFile = args[1];
                    int factorF = Integer.parseInt(args[2]);
                    int factorC = Integer.parseInt(args[3]);
                    CargarDatos.main(new String[]{csvFile, String.valueOf(factorF), String.valueOf(factorC)});
                    break;

                case "extraer":
                    if (args.length != 4) {
                        System.out.println("Uso para extraer: java -jar ProyectoBigDataPt1-1.0-SNAPSHOT.jar extraer <F> <C> <outputFile>");
                        return;
                    }
                    int extractFactorF = Integer.parseInt(args[1]);
                    int extractFactorC = Integer.parseInt(args[2]);
                    String outputFile = args[3];
                    extraerData.main(new String[]{String.valueOf(extractFactorF), String.valueOf(extractFactorC), outputFile});
                    break;

                default:
                    System.out.println("Comando no reconocido. Usa 'cargar' o 'extraer'.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Los factores deben ser números enteros." + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error de E/S al interactuar con HDFS."+ e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
