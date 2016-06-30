package com.ckstack.ckpush.service.accessory;

import java.util.Map;

/**
 * Created by dhkim94 on 15. 6. 9..
 */
public interface DeviceService {

    /**
     * 단말기 정보를 수정 한다.
     * modifyValue 에는 다음 값이 사용 가능 하다.
     *
     * @param deviceSrl
     * @param modifyValue
     */
    void modifyDevice(long deviceSrl, int appSrl, Map<String, String> modifyValue);
}
