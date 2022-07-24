package com.tre3p.fileserver.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestUtils {
    public static void writeDemoDataToFile(File fileToWrite, String data) throws IOException {
        FileWriter fw = new FileWriter(fileToWrite);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(data);
    }
}
