package com.bysj.emergencykg.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private long total;
    private List<T> records;
    public static <T> PageResult<T> empty() { return new PageResult<>(0L, Collections.emptyList()); }
    public static <T> PageResult<T> of(Page<T> page) { return new PageResult<>(page.getTotal(), page.getRecords()); }
    public static <S, T> PageResult<T> of(Page<S> page, Function<S, T> converter) {
        return new PageResult<>(page.getTotal(), page.getRecords().stream().map(converter).collect(Collectors.toList()));
    }
}
