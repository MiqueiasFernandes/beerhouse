package com.beerhouse.entity.spring;

import com.beerhouse.entity.IPage;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SpringPage implements IPage {

    private static int default_index_page = -1;
    private static int default_page_size = -1;
    private int page;
    private int size;

    public SpringPage(SpringDataWebProperties dataWebProperties) throws InstantiationException {
        if (dataWebProperties != null) {
            SpringDataWebProperties.Pageable pageable = dataWebProperties.getPageable();
            default_index_page = pageable.isOneIndexedParameters() ? 1 : 0;
            default_page_size = pageable.getDefaultPageSize();
        } else if (default_page_size < 0 || default_index_page < 0) {
            throw new InstantiationException("A pagina não foi configurada corretamente.");
        }
        this.page = default_index_page;
        this.size = default_page_size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(default_index_page, page);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size > 0 ? size : default_page_size;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page, size, sort == null ? Sort.unsorted() : sort);
    }

    @Override
    public int requestedItems() {
        return size;
    }

    @Override
    public String toString() {
        return "[Page: " + page + ", size: " + size + ']';
    }
}
