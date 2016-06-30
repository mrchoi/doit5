package com.ckstack.ckpush.data.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 4. 18..
 */
@Data
public class KeyBean {
    List<Integer> i_keys;
    List<Long> l_keys;
    List<Map<String, Object>> c_keys;
    Map<String, Object> m_key;
}
