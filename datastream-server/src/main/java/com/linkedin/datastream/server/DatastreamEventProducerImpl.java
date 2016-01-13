package com.linkedin.datastream.server;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkedin.datastream.server.api.schemaregistry.SchemaRegistryException;
import com.linkedin.datastream.server.api.schemaregistry.SchemaRegistryProvider;

/**
 * Implementation of the DatastremaEventProducer that connector will use to produce events. There is an unique
 * DatastreamEventProducerImpl object created per DatastreamTask that is assigned to the connector.
 * DatastreamEventProducer will inturn use a shared EventProducer (shared across the tasks that use the same destinations)
 * to produce the events.
 */
public class DatastreamEventProducerImpl implements DatastreamEventProducer {
  private static final Logger LOG = LoggerFactory.getLogger(DatastreamEventProducerImpl.class);

  private final SchemaRegistryProvider _schemaRegistryProvider;
  private final EventProducer _eventProducer;
  private final DatastreamTask _task;

  public DatastreamEventProducerImpl(DatastreamTask task, SchemaRegistryProvider schemaRegistryProvider,
      EventProducer eventProducer) {
    _schemaRegistryProvider = schemaRegistryProvider;
    _eventProducer = eventProducer;
    _task = task;
  }

  EventProducer getEventProducer() {
    return _eventProducer;
  }

  @Override
  public void send(DatastreamEventRecord event) {
    _eventProducer.send(_task, event);
  }

  /**
   * Register the schema in schema registry. If the schema already exists in the registry
   * Just return the schema Id of the existing
   * @param schema Schema that needs to be registered.
   * @return
   *   SchemaId of the registered schema.
   */
  @Override
  public String registerSchema(Schema schema) throws SchemaRegistryException {
    if (_schemaRegistryProvider != null) {
      return _schemaRegistryProvider.registerSchema(schema);
    } else {
      LOG.info("SchemaRegistryProvider is not configured, so registerSchema is not supported");
      throw new RuntimeException("SchemaRegistryProvider is not configured, So registerSchema is not supported");
    }
  }

  @Override
  public void flush() {
    _eventProducer.flush();
  }
}