package io.robusta.rra.microservices.saga.implementations;

import io.robusta.rra.microservices.saga.SagaAuditor;
import io.robusta.rra.microservices.saga.SharedContext;
import io.robusta.rra.utils.FileUtils;

import java.io.IOException;
import java.nio.file.*;

/**
 * Created by Nicolas Zozol on 26/12/2015.
 */
public class FileSagaAuditor implements SagaAuditor {

    Files file;
    Path path;


    public FileSagaAuditor(Path path) throws IOException {
        this.path = path;
        FileUtils.initFile(path);
    }



    public FileSagaAuditor(String path) throws IOException {
        this(Paths.get(path));
    }

    @Override
    public <T> void write(String category, String message, T data){
        try {
            Files.write(this.path, message.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            this.onFailAuditor(e.getMessage());
        }
    }


}
