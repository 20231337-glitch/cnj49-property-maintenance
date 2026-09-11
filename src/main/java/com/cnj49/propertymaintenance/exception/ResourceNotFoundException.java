package com.cnj49.propertymaintenance.exception;

/** Nem ra khi khong tim thay ban ghi theo id / ma. Duoc map sang trang 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entityName, Long id) {
        super("Không tìm thấy " + entityName + " với ID = " + id);
    }
}
