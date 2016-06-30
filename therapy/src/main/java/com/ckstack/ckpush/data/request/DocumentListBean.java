package com.ckstack.ckpush.data.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 8. 31..
 */
@Data
public class DocumentListBean {
    @NotNull(message = "{documentlistbean.category_srl.notNull}")
    @Min(value = 1, message = "{documentlistbean.category_srl.min}")
    private long category_srl;

    private String title;
    private List<Long> tags;
    private Map<String, String> sort;
    private int offset;
    private int limit;
    private boolean opt_content;
}
