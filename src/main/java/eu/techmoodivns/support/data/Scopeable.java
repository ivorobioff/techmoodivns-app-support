package eu.techmoodivns.support.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Scopeable implements Pageable {

    private int offset;
    private int limit;
    private Sort sort;

    public Scopeable(int offset, int limit) {
        this(offset, limit, Sort.unsorted());
    }

    public Scopeable(int offset, int limit, Sort sort) {
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public Scopeable(Scope scope) {
        this(scope.getOffset(), scope.getLimit(), scope.resolveSort());
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        throw new UnsupportedOperationException();
    }
}
