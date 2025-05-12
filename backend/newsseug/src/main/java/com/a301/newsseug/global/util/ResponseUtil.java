package com.a301.newsseug.global.util;

import com.a301.newsseug.global.model.dto.Result;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<EntityModel<Result<T>>> ok(Result<T> result) {
        return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(result));
    }

    public static <T> ResponseEntity<EntityModel<Result<T>>> ok(EntityModel<Result<T>> model) {
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    public static <T> ResponseEntity<CollectionModel<Result<T>>> ok(CollectionModel<Result<T>> model) {
        return ResponseEntity.status(HttpStatus.OK).body(model);
    }

    public static <T> ResponseEntity<Result<T>> created(Result<T> result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    public static <T> ResponseEntity<EntityModel<Result<T>>> created(EntityModel<Result<T>> model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

}
