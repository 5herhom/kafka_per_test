package cn.com.sherhom.reno.http.multi.thread.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import cn.com.sherhom.reno.boot.record.TimeCostStat;
/**
 * @author Sherhom
 * @date 2020/10/28 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpMultiThreadArgs {
    Long lastTimeMs;
    TimeCostStat stat;
}
