package com.beerhouse.entity.spring;

import com.beerhouse.entity.IEntity;
import org.springframework.http.ResponseEntity;

public class SpringEntity<T> extends ResponseEntity<T> implements IEntity {

    public SpringEntity(ResponseEntity<T> responseEntity) {
        super(responseEntity.getBody(), responseEntity.getHeaders(), responseEntity.getStatusCode());
    }

    public static SpringEntity fromResponse(ResponseEntity responseEntity) {
        return new SpringEntity(responseEntity);
    }

    @Override
    public String printableEntity() {
        return this.getBody().toString();
    }
}
