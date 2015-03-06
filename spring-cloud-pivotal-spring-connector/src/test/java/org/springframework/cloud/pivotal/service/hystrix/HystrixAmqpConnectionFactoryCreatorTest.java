package org.springframework.cloud.pivotal.service.hystrix;

import org.junit.Test;
import org.springframework.cloud.service.common.AmqpServiceInfo;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the Hystrix AMQP connection factory creator
 *
 * @author Scott Frederick
 */
public class HystrixAmqpConnectionFactoryCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final int TEST_PORT = 1234;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";
	private static final String TEST_VHOST = "vhost1";

	private HystrixAmqpConnectionFactoryCreator creator = new HystrixAmqpConnectionFactoryCreator();

	@Test
	public void cloudRabbitCreationNoConfig() throws Exception {
		AmqpServiceInfo serviceInfo = createServiceInfo();

		ConnectionFactory connector = creator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, connector);
	}

	public AmqpServiceInfo createServiceInfo() {
		return new AmqpServiceInfo("id", TEST_HOST, TEST_PORT, TEST_USERNAME, TEST_PASSWORD, TEST_VHOST);
	}

	private void assertConnectorProperties(AmqpServiceInfo serviceInfo, ConnectionFactory connector) {
		assertNotNull(connector);

		assertEquals(serviceInfo.getHost(), connector.getHost());
		assertEquals(serviceInfo.getPort(), connector.getPort());
		com.rabbitmq.client.ConnectionFactory underlying = (com.rabbitmq.client.ConnectionFactory) ReflectionTestUtils.getField(connector, "rabbitConnectionFactory");
		assertEquals(serviceInfo.getUserName(), ReflectionTestUtils.getField(underlying, "username"));
		assertEquals(serviceInfo.getPassword(), ReflectionTestUtils.getField(underlying, "password"));

		assertEquals(serviceInfo.getVirtualHost(), connector.getVirtualHost());
	}
}
