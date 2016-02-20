package com.epam.freelancer.business.file;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    public static final Logger LOG = Logger.getLogger(ReadFile.class);

    public String read(String fileName) throws FileNotFoundException {
        StringBuffer sb = new StringBuffer();

        try (FileReader in = new FileReader(fileName);
             BufferedReader br = new BufferedReader(in)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            LOG.error(e);
        }

        return sb.toString();
    }
}
