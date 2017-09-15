
/*******************************************************************************
 *   BSD License
 *    
 *   Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.
 *    
 *   Redistribution and use in source and binary forms, with or without modification, are permitted
 *   provided that the following conditions are met:
 *    
 *   1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *      and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *      conditions and the following disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   3. All advertising materials mentioning features or use of this software must display the
 *      following acknowledgement:  This product includes software developed by the AT&T.
 *   4. Neither the name of AT&T nor the names of its contributors may be used to endorse or
 *      promote products derived from this software without specific prior written permission.
 *    
 *   THIS SOFTWARE IS PROVIDED BY AT&T INTELLECTUAL PROPERTY ''AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 *   SHALL AT&T INTELLECTUAL PROPERTY BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS;
 *   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 *   DAMAGE.
 *******************************************************************************/
package com.att.tta.rs;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.Filter;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This Class provides configuration to setup Elastic Search.
 * 
 * @author ak983d
 *
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.att.tta.rs.data.es.repository")
@ComponentScan(basePackages = "com.att.tta")
@PropertySource("classpath:resiliencystudio-${envTarget:dev}.properties")
@Import({ SecurityConfig.class })
public class AppConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static Logger logr  = LoggerFactory.getLogger(AppConfiguration.class);

	@Value("${elasticsearch.home:/opt/app/workload/elasticsearch}")
	private String elasticsearchHome;

	@Autowired
	Environment env;

	/** Elastic Search cluster name */
	@Value("${resiliencystudio.recorder.elasticsearch.cluster}")
	private String elasticSearchCluster;

	/** Elastic Search cluster Host */
	@Value("${resiliencystudio.recorder.elasticsearch.host}")
	private String elasticSearchHost;

	private static Client client;

	/**
	 * This is a getter method for Elastic Search client object.
	 * 
	 * @return the client
	 */
	public static Client getClient() {
		return client;
	}

	/**
	 * This is a setter method for Elastic Search client object.
	 * @param client
	 *       
	 */
	public static void setClient(Client client) {
		AppConfiguration.client = client;
	}

	@Bean
	public Client client() {
		if (client == null) {
			logr.debug("Connecting to elastic search server");

			elasticSearchCluster = env.getProperty("resiliencystudio.recorder.elasticsearch.cluster");
			if (elasticSearchCluster == null) {
				elasticSearchCluster = "attsa2";
			}
			elasticSearchHost = env.getProperty("resiliencystudio.recorder.elasticsearch.host");
			if (elasticSearchHost == null) {
				elasticSearchHost = "localhost";
			}

			final Settings.Builder elasticsearchSettings = Settings.settingsBuilder().put("http.enabled", "false")
					.put("cluster.name", elasticSearchCluster);

			logr.debug("elasticSearchCluster--> %d", elasticSearchCluster);
			logr.debug("elasticSearchHost --> %d", elasticSearchHost);

			try {
				setClient(TransportClient.builder().settings(elasticsearchSettings).build().addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(elasticSearchHost), 9300)));
			} catch (UnknownHostException e) {
				logr.error("Not able to connect to Elastic Search: ", e);
			}
		}
		return client;
	}

	@Bean
	public NodeBuilder nodeBuilder() {
		return new NodeBuilder();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		ElasticsearchTemplate es = null;
		;
		try {
			es = new ElasticsearchTemplate(client());
		} catch (Exception ex) {
			logr.error("Exception occured while creating Elastic Search Template: ", ex);
		}
		return es;
	}

	@Override
	protected void finalize() throws Throwable {
		if (client != null) {
			logr.debug("Closing connection to elastic search ");
			client.close();
		}
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(5);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public RequestContextFilter requestContextFilter() {
		return new RequestContextFilter();
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] {};
	}

}