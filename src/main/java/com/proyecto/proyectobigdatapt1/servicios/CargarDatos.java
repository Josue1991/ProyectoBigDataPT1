/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.proyectobigdatapt1.servicios;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import org.apache.hadoop.hbase.TableName;

/**
 *
 * @author Josue
 */
public class CargarDatos {

    private static final String TABLE_NAME = "ElectricMeasurements";
    private static final String COLUMN_FAMILY = "cf";

    public static void main(String[] args) throws IOException, CsvValidationException {
        String csvFile = args[0];
        int factorF = Integer.parseInt(args[1]);
        int factorC = Integer.parseInt(args[2]);

        Configuration config = HBaseConfiguration.create();
        try (Connection connection = ConnectionFactory.createConnection(config); Admin admin = connection.getAdmin()) {
            // Crear tabla si no existe
            if (!admin.tableExists(TableName.valueOf(TABLE_NAME))) {
                TableDescriptor tableDescriptor = TableDescriptorBuilder
                        .newBuilder(TableName.valueOf(TABLE_NAME))
                        .setColumnFamily(ColumnFamilyDescriptorBuilder.of(COLUMN_FAMILY))
                        .build();
                admin.createTable(tableDescriptor);
            }

            try (Table table = connection.getTable(TableName.valueOf(TABLE_NAME)); CSVReader reader = new CSVReader(new FileReader(csvFile))) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    String sensor = nextLine[0];
                    String datetime = nextLine[1];
                    String measure = nextLine[2];

                    // Aplicar bootstrapping
                    for (int f = 1; f <= factorF; f++) {
                        for (int c = 1; c <= factorC; c++) {
                            String rowKey = f + sensor + "_" + datetime;
                            Put put = new Put(Bytes.toBytes(rowKey));
                            put.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("measure" + c), Bytes.toBytes(measure));
                            table.put(put);
                        }
                    }
                }
            }
        }
    }
}
