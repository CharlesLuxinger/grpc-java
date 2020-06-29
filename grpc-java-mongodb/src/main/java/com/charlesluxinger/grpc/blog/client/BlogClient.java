package com.charlesluxinger.grpc.blog.client;

import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.DeleteBlogRequest;
import com.proto.blog.DeleteBlogResponse;
import com.proto.blog.FindAllBlogRequest;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import com.proto.blog.UpdateBlogRequest;
import com.proto.blog.UpdateBlogResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.proto.blog.BlogServiceGrpc.*;

public class BlogClient {
	public static void main(String[] args) {
		System.out.println("Client starting...");

		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build();

		BlogServiceBlockingStub blogClient = newBlockingStub(channel);

		CreateBlogResponse response = blogClient.createBlog(CreateBlogRequest.newBuilder()
																			 .setBlog(Blog.newBuilder()
																						  .setAuthorId("Charles")
																						  .setTitle("Learning gRPC")
																						  .setContent("First CRUD with GRPC")
																						  .build())
																			 .build());

		System.out.println("Received create Blog response");
		System.out.println(response.toString());

		System.out.println("Reading blog...");
		String blogId = response.getBlog().getId();

		ReadBlogResponse blogResponse = blogClient.readBlog(ReadBlogRequest.newBuilder().setBlogId(blogId).build());
		System.out.println("Received Blog response");
		System.out.println(blogResponse.toString());

//		System.out.println("Reading blog with non existing id...");
//		blogClient.readBlog(ReadBlogRequest.newBuilder().setBlogId("52e03f68d7e8ce28b171c203").build());

		System.out.println("Updating blog...");
		UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(UpdateBlogRequest.newBuilder()
																					   .setBlog(Blog.newBuilder()
																									.setId(response.getBlog().getId())
																									.setAuthorId("Manuel")
																									.setTitle("Learning GO")
																									.setContent("First CRUD with GO")
																									.build())
																					   .build());

		System.out.println("Updated blog!");
		System.out.println(updateBlogResponse.toString());

		System.out.println("Deleting blog...");
		DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(DeleteBlogRequest.newBuilder()
																					   .setBlogId(response.getBlog().getId())
																					   .build());

		System.out.println("Deleted blog!");
		System.out.println(deleteBlogResponse.toString());

//		System.out.println("Deleting blog with non existing id...");
//		blogClient.deleteBlog(DeleteBlogRequest.newBuilder().setBlogId("52e03f68d7e8ce28b171c203").build());

		System.out.println("Finding All blogs...");

		blogClient.findAllBlog(FindAllBlogRequest.newBuilder().build())
				  .forEachRemaining(blog -> System.out.println(blog.toString()));

		System.out.println("Shutting down channel");
		channel.shutdown();


	}
}
