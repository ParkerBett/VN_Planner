package com.vnplanner.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnplanner.model.Project;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ProjectIO {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String ENTRY_NAME = "project.json";

    public static void saveAsVnproj(File file, Project project) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zos.putNextEntry(new ZipEntry(ENTRY_NAME));
            byte[] json = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(project);
            zos.write(json);
            zos.closeEntry();
        }
    }

    public static Project loadFromVnproj(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (ENTRY_NAME.equals(entry.getName())) {
                    return mapper.readValue(zis, Project.class);
                }
            }
        }
        throw new FileNotFoundException("project.json not found inside vnproj");
    }

    public static void exportJson(File file, Project project) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fos, project);
        }
    }

    public static Project importJson(File file) throws IOException {
        try (InputStream is = Files.newInputStream(file.toPath())) {
            return mapper.readValue(is, Project.class);
        }
    }
}
