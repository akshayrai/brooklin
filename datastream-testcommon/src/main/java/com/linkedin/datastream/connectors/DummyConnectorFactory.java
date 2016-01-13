package com.linkedin.datastream.connectors;

import java.util.Properties;

import com.linkedin.datastream.server.api.connector.Connector;
import com.linkedin.datastream.server.api.connector.ConnectorFactory;


public class DummyConnectorFactory implements ConnectorFactory {
  @Override
  public Connector createConnector(Properties config) {
    try {
      return new DummyConnector(config);
    } catch (Exception e) {
      throw new RuntimeException("Instantiating DummyConnector threw exception", e);
    }
  }
}