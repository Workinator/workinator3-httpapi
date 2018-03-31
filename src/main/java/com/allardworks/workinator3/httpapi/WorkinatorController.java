package com.allardworks.workinator3.httpapi;

import com.allardworks.workinator3.core.*;
import com.allardworks.workinator3.core.commands.CreatePartitionCommand;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Created by jaya on 2/28/18.
 * k?
 */
@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class WorkinatorController {
    private final Workinator workinator;

    @Data
    public static class ConsumerMini {
        private final String name;
        private final List<String> assignedPartitions;
    }

    @GetMapping("consumers")
    public Iterable<ConsumerInfo> getConsumers() {
        return workinator.getConsumers();
    }

    @GetMapping("consumers/mini")
    public Iterable<ConsumerMini> getConsumersMini() {
        return StreamSupport
                .stream(getConsumers().spliterator(), false)
                .map(c -> {
                        val workers =
                                c
                                .getWorkers()
                                .stream()
                                .map(ConsumerWorkerInfo::getPartitionKey)
                                .filter(Objects::nonNull)
                                .collect(toList());
                        return new ConsumerMini(c.getName(), workers);
                })
                .collect(toList());
    }

    @GetMapping("partitions")
    public List<PartitionInfo> getPartitions() {
        return workinator.getPartitions();
    }

    @GetMapping("partitions/{partitionKey}")
    public PartitionInfo getPartition(@PathVariable("partitionKey") final String partitionKey) {
        val partitionInfo = getPartitions().stream().filter(p -> p.getPartitionKey().equals(partitionKey)).findFirst();
        // TODO: 404
        return partitionInfo.orElse(null);
    }

    @PutMapping("partitions/{partitionKey}")
    public PartitionInfo createPartition(@PathVariable("partitionKey") String partitionKey, @RequestBody final CreatePartitionRequest request) throws PartitionExistsException {
        // TODO: currently results in 500 if partition exists.
        // should be idempotent - update the config
        val createCommand = CreatePartitionCommand
                .builder()
                .partitionKey(partitionKey)
                .maxWorkerCount(request.getMaxWorkerCount())
                .maxIdleTimeSeconds(request.getMaxIdleTimeSeconds())
                .build();
        workinator.createPartition(createCommand);
        return getPartition(partitionKey);
    }
}
