package com.nezha.component.lock;

import com.nezha.component.model.LockInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @author: nezha <br>
 * @Title: WriteLock <br>
 * @ProjectName: zklock-spring-boot-starter <br>
 * @Description: xxxxx <br>
 * @Date: 2019/9/19 12:42 PM <br>
 */
public class WriteLock implements Lock {
    private InterProcessReadWriteLock lock;

    private final LockInfo lockInfo;

    private CuratorFramework zkClient;

    public WriteLock(CuratorFramework zkClient, LockInfo lockInfo) {
        this.zkClient = zkClient;
        this.lockInfo = lockInfo;
    }
    @Override
    public boolean acquire() {
        try {
            lock = new InterProcessReadWriteLock(zkClient, lockInfo.getName());
            return lock.writeLock().acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (lock.writeLock().isAcquiredInThisProcess()) {
            try {
                lock.writeLock().release();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
