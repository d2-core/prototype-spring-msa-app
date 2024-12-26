package com.d2.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class R2Config {
	@Value("${aws.s3.endpointUrl:null}")
	private String s3EndpointUrl;

	@Value("${aws.s3.region:null}")
	private String s3Region;

	@Value("${aws.accessKey:null}")
	private String awsAccessKey;

	@Value("${aws.secretKey:null}")
	private String awsSecretKey;

	@Bean
	public AmazonS3 s3Client() {
		if (s3EndpointUrl != null && s3Region != null && awsAccessKey != null && awsSecretKey != null) {
			BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);

			return AmazonS3ClientBuilder
				.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3EndpointUrl, s3Region))
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
		}
		
		return null;
	}
}
