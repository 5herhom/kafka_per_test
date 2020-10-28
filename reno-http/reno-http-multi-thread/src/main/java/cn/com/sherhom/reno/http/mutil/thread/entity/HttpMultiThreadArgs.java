package cn.com.sherhom.reno.http.mutil.thread.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CountDownLatch;
import cn.com.sherhom.reno.http.common.record.Stat;
/**
 * @author Sherhom
 * @date 2020/10/28 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMultiThreadArgs {
    Long lastTimeMs;
    Stat stat;
}
