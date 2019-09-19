package com.nezha.component.lock;

import com.nezha.component.model.LockInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @author: nezha <br>
 * @Title: ReadLock <br>
 * @ProjectName: zklock-spring-boot-starter <br>
 * @Description: 实现读锁 <br>
 * @Date: 2019/9/19 12:34 PM <br>
 */
public class ReadLock implements Lock {

    private InterProcessReadWriteLock lock;

    private final LockInfo lockInfo;

    private CuratorFramework zkClient;

    public ReadLock(CuratorFramework zkClient, LockInfo lockInfo) {
        this.zkClient = zkClient;
        this.lockInfo = lockInfo;
    }
    @Override
    public boolean acquire() {
        try {
            lock = new InterProcessReadWriteLock(zkClient, lockInfo.getName());
            return lock.readLock().acquire(lockInfo.getWaitTime(), TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (lock.readLock().isAcquiredInThisProcess()) {
            try {
                lock.readLock().release();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
