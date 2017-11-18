package com.tradeshift.auditserver.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@AllArgsConstructor
public class AMPQConfiguration {

    private final AuditServerGatewayProperties apiserverProperties;

    @Bean
    public Queue queue() {
        return new Queue(apiserverProperties.getQueueName(), true, false, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(apiserverProperties.getExchangeName());
    }

    @Bean
    public RabbitTransactionManager rabbitTransactionManager(ConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        return jsonConverter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(10.0);
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRetryTemplate(retryTemplate);
        template.setChannelTransacted(true);
        template.setMandatory(true);
        template.setMessageConverter(messageConverter);

        return template;
    }

    @Bean
    public RabbitAdmin ampqAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("#");
    }

    // This should not be here... but for now it will do :/
    @Bean
    public ApplicationRunner initializeRabbitMQ(RabbitAdmin rabbitAdmin, Queue queue, TopicExchange exchange) {
        return args -> {
            // make the queue HA (mirrored)
            // rabbitmqctl set_policy ha-all "^ha\." '{"ha-mode":"all"}'
            rabbitAdmin.declareQueue(queue);

            rabbitAdmin.declareExchange(exchange);

            Binding apiServerBinding = BindingBuilder.bind(queue).to(exchange).with("#");
            rabbitAdmin.declareBinding(apiServerBinding);

            Queue pulseQueue = new Queue("ha.pulse-queue", true, false, false);

            rabbitAdmin.declareQueue(pulseQueue);
            Binding pulseBinding = BindingBuilder.bind(pulseQueue).to(exchange).with("#");
            rabbitAdmin.declareBinding(pulseBinding);
        };
    }


}
