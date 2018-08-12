/*
 * Copyright (c) 2018. Aberic - All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.kkgy333.sword.fabric.server.service.impl;

import com.github.kkgy333.sword.fabric.server.bean.*;
import com.github.kkgy333.sword.fabric.server.dao.Block;
import com.github.kkgy333.sword.fabric.server.dao.Channel;
import com.github.kkgy333.sword.fabric.server.dao.mapper.BlockMapper;
import com.github.kkgy333.sword.fabric.server.service.BlockService;
import com.github.kkgy333.sword.fabric.server.utils.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author : Aberic 【2018-08-10 16:24】
 */
@Service("blockService")
public class BlockServiceImpl implements BlockService {

    @Resource
    private BlockMapper blockMapper;

    @Override
    public int add(Block block) {
        return blockMapper.add(block);
    }

    @Override
    public List<ChannelPercent> getChannelPercents(List<Channel> channels) {
        List<ChannelPercent> channelPercents = new LinkedList<>();
        for (Channel channel: channels) {
            int txCount = 0;
            try {
                txCount = blockMapper.countTxByChannelId(channel.getId());
            } catch (Exception ignored) {

            }
            ChannelPercent channelPercent = new ChannelPercent();
            channelPercent.setName(channel.getName());
            channelPercent.setBlockPercent(blockMapper.countByChannelId(channel.getId()));
            channelPercent.setTxPercent(txCount);
            channelPercents.add(channelPercent);
        }
        return channelPercents;
    }

    @Override
    public List<ChannelBlockList> getChannelBlockLists(List<Channel> channels) {
        int today = Integer.valueOf(DateUtil.getCurrent("yyyyMMdd"));
        List<ChannelBlockList> channelBlockLists = new LinkedList<>();
        List<ChannelBlock> channelBlocks = new LinkedList<>();
        for (Channel channel: channels) {
            int zeroCount = 0;
            for (int i = 0; i < 20; i++) {
                int date = today - i;
                int blockCount = blockMapper.countByChannelIdAndDate(channel.getId(), date);
                if (blockCount == 0) {
                    zeroCount++;
                }
                ChannelBlock channelBlock = new ChannelBlock();
                channelBlock.setName(channel.getName());
                channelBlock.setBlocks(blockCount);
                channelBlock.setDate(String.valueOf(date));
                channelBlocks.add(channelBlock);
            }
            ChannelBlockList channelBlockList = new ChannelBlockList();
            channelBlockList.setChannelBlocks(channelBlocks);
            channelBlockList.setZeroCount(zeroCount);
            channelBlockLists.add(channelBlockList);
        }
        channelBlockLists.sort((t1, t2) -> Math.toIntExact(t2.getZeroCount() - t1.getZeroCount()));
        return channelBlockLists;
    }

    @Override
    public DayStatistics getDayStatistics() {
        int today = Integer.valueOf(DateUtil.getCurrent("yyyyMMdd"));
        int todayBlocks = blockMapper.countByDate(today);
        int todayTxs = 0;
        int allTxs = 0;
        try {
            todayTxs = blockMapper.countTxByDate(today);
        } catch (Exception ignored) {

        }
        try {
            allTxs = blockMapper.countTx();
        } catch (Exception ignored) {

        }
        int allBlocks = blockMapper.count();
        DayStatistics dayStatistics = new DayStatistics();
        dayStatistics.setBlockCount(todayBlocks);
        dayStatistics.setTxCount(todayTxs);
        dayStatistics.setBlockPercent(todayBlocks == 0 ? 0 : (1 - todayBlocks/allBlocks) * 100);
        dayStatistics.setTxPercent(todayTxs == 0 ? 0 : (1 - todayTxs/allTxs) * 100);
        return dayStatistics;
    }

    @Override
    public Platform getPlatform() {
        int txCount = 0;
        int rwSetCount = 0;
        try {
            txCount = blockMapper.countTx();
        } catch (Exception ignored) {

        }
        try {
            rwSetCount = blockMapper.countRWSet();
        } catch (Exception ignored) {

        }
        Platform platform = new Platform();
        platform.setBlockCount(blockMapper.count());
        platform.setTxCount(txCount);
        platform.setRwSetCount(rwSetCount);
        return platform;
    }

}
