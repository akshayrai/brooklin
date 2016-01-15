package com.linkedin.datastream.server.assignment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedin.datastream.common.Datastream;
import com.linkedin.datastream.server.AssignmentStrategy;
import com.linkedin.datastream.server.DatastreamTask;
import com.linkedin.datastream.server.DatastreamTaskImpl;


public class BroadcastStrategy implements AssignmentStrategy {

  private static final Logger LOG = LoggerFactory.getLogger(BroadcastStrategy.class.getName());

  @Override
  public Map<String, Set<DatastreamTask>> assign(List<Datastream> datastreams, List<String> instances,
      Map<String, Set<DatastreamTask>> currentAssignment) {

    Map<String, Set<DatastreamTask>> assignment = new HashMap<>();

    for (String instance : instances) {

      Set<DatastreamTask> newAssignmentForInstance = new HashSet<>();
      assignment.put(instance, newAssignmentForInstance);


      Map<String, DatastreamTask> datastreamToTaskMap = new HashMap<>();
      Set<DatastreamTask> currentAssignmentForInstance = currentAssignment.containsKey(instance) ?
          currentAssignment.get(instance) :  new HashSet<>();

      for (DatastreamTask datastreamTask : currentAssignmentForInstance) {
        datastreamTask.getDatastreams().stream().forEach(d -> datastreamToTaskMap.put(d, datastreamTask));
      }

      for (Datastream datastream : datastreams) {
        DatastreamTask foundDatastreamTask = datastreamToTaskMap.containsKey(datastream.getName()) ?
            datastreamToTaskMap.get(datastream.getName()) : new DatastreamTaskImpl(datastream);
        newAssignmentForInstance.add(foundDatastreamTask);
      }
    }

    return assignment;
  }
}
