package eu.techmoodivns.support.data;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Sort.Direction;

import static org.springframework.data.domain.Sort.Direction.ASC;

public class Scope {
    private Integer offset = 0;
    private Integer limit = 20;
    private String sort;

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public Sort resolveSort() {

        Sort defaultSort = Sort.unsorted();

        if (sort == null) {
            return defaultSort;
        }

        String[] parsedSort = sort.split(":");

        if (parsedSort.length == 0 || parsedSort.length > 2) {
            return defaultSort;
        }

        String field = parsedSort[0];

        Direction direction = parsedSort.length > 1
                ? Direction.fromOptionalString(parsedSort[1]).orElse(ASC)
                : ASC;

        return Sort.by(direction, field);
    }
}
