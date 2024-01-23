package dev.kwolszczak.peopledb.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {
    public StorageException(IOException e) {
    }
}
