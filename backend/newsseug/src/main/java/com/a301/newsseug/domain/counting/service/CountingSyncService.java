package com.a301.newsseug.domain.counting.service;

public interface CountingSyncService {

    void scheduledSyncViewCounting();
    void scheduledSyncLikeCounting();
    void scheduledSyncHateCounting();

}
