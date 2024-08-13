/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.proyectobigdatapt1.servicios;

/**
 *
 * @author Josue
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.hadoop.hbase.TableName;

public class extraerData {

    private static final String TABLE_NAME = "ElectricMeasurements";
    private static final String COLUMN_FAMILY = "cf";

    public static void main(String[] args) throws IOException {
        int factorF = Integer.parseInt(args[0]);
        int factorC = Integer.parseInt(args[1]);
        String outputFile = args[2];

        Configuration config = HBaseConfiguration.create();
        try (Connection connection = ConnectionFactory.createConnection(config); Table table = connection.getTable(TableName.valueOf(TABLE_NAME)); FileWriter writer = new FileWriter(outputFile)) {

            // Recuperar datos
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(COLUMN_FAMILY));
            try (ResultScanner scanner = table.getScanner(scan)) {
                for (Result result : scanner) {
                    String rowKey = Bytes.toString(result.getRow());
                    if (rowKey.startsWith(factorF + "")) {
                        String sensor = rowKey.substring(1, rowKey.indexOf("_"));
                        String datetime = rowKey.substring(rowKey.indexOf("_") + 1);
                        String measure = Bytes.toString(result.getValue(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes("measure" + factorC)));

                        writer.append(sensor).append(",").append(datetime).append(",").append(measure).append("\n");
                    }
                }
            }
        }
    }
}
